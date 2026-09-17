# Image Processing Patterns Reference

Canonical patterns for image processing with Sharp.js and ImageMagick.

## Sharp.js (Node.js — Recommended for Web)
```ts
// src/lib/image.ts
import sharp from "sharp";
import { createId } from "@paralleldrive/cuid2";
import path from "path";

// Resize and optimize for web delivery
export async function optimizeImage(
  inputBuffer: Buffer,
  options: {
    width?: number;
    height?: number;
    format?: "webp" | "avif" | "jpeg" | "png";
    quality?: number;
  } = {}
): Promise<{ buffer: Buffer; format: string; size: number }> {
  const { width = 1920, height, format = "webp", quality = 80 } = options;

  let pipeline = sharp(inputBuffer).resize(width, height, {
    fit: "inside",             // Maintain aspect ratio, fit within bounds
    withoutEnlargement: true,  // Don't upscale small images
  });

  let outputBuffer: Buffer;
  switch (format) {
    case "webp":
      outputBuffer = await pipeline.webp({ quality }).toBuffer();
      break;
    case "avif":
      outputBuffer = await pipeline.avif({ quality }).toBuffer();
      break;
    case "jpeg":
      outputBuffer = await pipeline.jpeg({ quality, progressive: true }).toBuffer();
      break;
    case "png":
      outputBuffer = await pipeline.png({ compressionLevel: 9 }).toBuffer();
      break;
  }

  return { buffer: outputBuffer, format, size: outputBuffer.length };
}

// Generate responsive image sizes
export async function generateResponsiveImages(
  inputBuffer: Buffer,
  outputDir: string,
  slug: string
): Promise<{ width: number; path: string; size: number }[]> {
  const sizes = [320, 640, 1024, 1920];
  const results = await Promise.all(
    sizes.map(async (width) => {
      const { buffer } = await optimizeImage(inputBuffer, { width, format: "webp", quality: 82 });
      const outputPath = path.join(outputDir, `${slug}-${width}w.webp`);
      await sharp(buffer).toFile(outputPath);
      return { width, path: outputPath, size: buffer.length };
    })
  );
  return results;
}
```

## Thumbnail Generation
```ts
// src/lib/thumbnail.ts
// Crop to exact dimensions (for avatars, product thumbnails)
export async function generateThumbnail(
  inputBuffer: Buffer,
  size: { width: number; height: number } = { width: 400, height: 400 }
): Promise<Buffer> {
  return sharp(inputBuffer)
    .resize(size.width, size.height, {
      fit: "cover",        // Crop to fill exact dimensions
      position: "center",  // Center the crop
    })
    .webp({ quality: 85 })
    .toBuffer();
}

// Generate image with watermark
export async function addWatermark(
  inputBuffer: Buffer,
  watermarkPath: string,
  position: "top-right" | "bottom-right" | "center" = "bottom-right"
): Promise<Buffer> {
  const watermark = await sharp(watermarkPath)
    .resize(200, null, { fit: "inside" })
    .toBuffer();

  const input = sharp(inputBuffer);
  const { width = 1920, height = 1080 } = await input.metadata();

  // Calculate gravity position
  const gravity = {
    "top-right": sharp.gravity.northeast,
    "bottom-right": sharp.gravity.southeast,
    center: sharp.gravity.center,
  }[position];

  return input
    .composite([{ input: watermark, gravity }])
    .webp({ quality: 90 })
    .toBuffer();
}
```

## Upload Handler with Processing Pipeline
```ts
// src/routes/uploads.ts — multipart upload with processing
import { Hono } from "hono";
import { S3Client, PutObjectCommand } from "@aws-sdk/client-s3";
import { validateImageBuffer } from "@/lib/validate-media";

const s3 = new S3Client({ region: process.env.AWS_REGION! });

export const uploadRouter = new Hono()
  .post("/images", async (c) => {
    const body = await c.req.parseBody();
    const file = body.file as File;

    if (!file || !(file instanceof File)) {
      return c.json({ error: "No file provided" }, 400);
    }

    // Size limit: 10MB
    if (file.size > 10 * 1024 * 1024) {
      return c.json({ error: "File too large (max 10MB)" }, 413);
    }

    const inputBuffer = Buffer.from(await file.arrayBuffer());

    // Validate using magic bytes (not file extension)
    const { valid, mimeType } = await validateImageBuffer(inputBuffer);
    if (!valid) {
      return c.json({ error: `Unsupported file type: ${mimeType}` }, 415);
    }

    // Process: optimize + generate thumbnails
    const [optimized, thumbnail] = await Promise.all([
      optimizeImage(inputBuffer, { width: 1920, format: "webp", quality: 85 }),
      generateThumbnail(inputBuffer, { width: 400, height: 400 }),
    ]);

    const id = createId();
    const [originalKey, thumbKey] = [`images/${id}/original.webp`, `images/${id}/thumb.webp`];

    // Upload to S3
    await Promise.all([
      s3.send(new PutObjectCommand({
        Bucket: process.env.S3_BUCKET!,
        Key: originalKey,
        Body: optimized.buffer,
        ContentType: "image/webp",
        CacheControl: "public, max-age=31536000, immutable",
      })),
      s3.send(new PutObjectCommand({
        Bucket: process.env.S3_BUCKET!,
        Key: thumbKey,
        Body: thumbnail,
        ContentType: "image/webp",
        CacheControl: "public, max-age=31536000, immutable",
      })),
    ]);

    const cdnBase = process.env.CDN_URL!;
    return c.json({
      id,
      url: `${cdnBase}/${originalKey}`,
      thumbnailUrl: `${cdnBase}/${thumbKey}`,
      size: optimized.size,
    }, 201);
  });
```

## MIME Type Validation (Magic Bytes)
```ts
// src/lib/validate-media.ts
// Validate using file magic bytes, never trust file extension
const IMAGE_MAGIC_BYTES: { bytes: number[]; mime: string }[] = [
  { bytes: [0xFF, 0xD8, 0xFF], mime: "image/jpeg" },
  { bytes: [0x89, 0x50, 0x4E, 0x47], mime: "image/png" },
  { bytes: [0x47, 0x49, 0x46], mime: "image/gif" },
  { bytes: [0x52, 0x49, 0x46, 0x46], mime: "image/webp" },  // RIFF....WEBP
  { bytes: [0x00, 0x00, 0x00, 0x1C, 0x66, 0x74, 0x79, 0x70], mime: "image/avif" },
];

export function validateImageBuffer(buffer: Buffer): { valid: boolean; mimeType: string } {
  for (const { bytes, mime } of IMAGE_MAGIC_BYTES) {
    if (bytes.every((byte, i) => buffer[i] === byte)) {
      // Extra WebP check: bytes 8-11 must be "WEBP"
      if (mime === "image/webp" && buffer.slice(8, 12).toString() !== "WEBP") continue;
      return { valid: true, mimeType: mime };
    }
  }
  return { valid: false, mimeType: "unknown" };
}
```

## ImageMagick CLI (for complex operations)
```bash
# Convert and resize
magick input.jpg -resize 800x600\> -quality 85 output.webp

# Batch convert directory
magick mogrify -format webp -quality 85 -resize 1920x1080\> *.jpg

# Strip metadata (EXIF) for privacy
magick input.jpg -strip output.jpg

# Generate sprite sheet (contact sheet)
magick montage frame*.jpg -geometry 100x100+2+2 -background black sprite.jpg

# Apply blur for placeholder (LQIP - Low Quality Image Placeholder)
magick input.jpg -resize 32x32 -blur 0x4 -quality 30 placeholder.jpg
```

## Performance Considerations

| Operation | Sharp (Node.js) | ImageMagick CLI | Notes |
|---|---|---|---|
| Single image resize | ~20ms | ~150ms | Sharp uses libvips — much faster |
| Batch processing | Parallel streams | Mogrify batch | Sharp preferred for server-side API |
| Complex compositing | Limited | Full support | ImageMagick better for multi-layer ops |
| WebP/AVIF output | Native | Requires build flag | Sharp has better format support |
| Memory usage | ~50MB/image | ~200MB/image | Sharp is significantly more efficient |

## Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| Loading full image into memory for large files | Use Sharp streaming: `sharp(inputStream).pipe(outputStream)` |
| Trusting file extension for MIME validation | Check magic bytes with `validateImageBuffer()` |
| Storing processed images on local disk | Upload to S3/R2/GCS; local disk doesn't survive restarts |
| Synchronous image processing on HTTP thread | Queue with BullMQ for images > 500KB |
| No size limit on upload | Set `maxBodySize` in framework config and validate `file.size` |
