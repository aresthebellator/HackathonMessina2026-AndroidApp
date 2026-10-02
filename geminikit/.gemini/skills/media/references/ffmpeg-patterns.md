# FFmpeg Processing Patterns Reference

Canonical patterns for video and audio processing with FFmpeg.

## FFmpeg Node.js Integration (fluent-ffmpeg)
```ts
// src/lib/ffmpeg.ts
import ffmpeg from "fluent-ffmpeg";
import { path as ffmpegPath } from "@ffmpeg-installer/ffmpeg";
import { path as ffprobePath } from "@ffprobe-installer/ffprobe";
import { promises as fs } from "fs";
import path from "path";

ffmpeg.setFfmpegPath(ffmpegPath);
ffmpeg.setFfprobePath(ffprobePath);

// Probe media file for metadata
export function probeMedia(inputPath: string): Promise<ffmpeg.FfprobeData> {
  return new Promise((resolve, reject) => {
    ffmpeg.ffprobe(inputPath, (err, data) => {
      if (err) reject(new Error(`Probe failed: ${err.message}`));
      else resolve(data);
    });
  });
}

// Convert video to MP4 (H.264 + AAC)
export function transcodeToMp4(
  inputPath: string,
  outputPath: string,
  options: { width?: number; height?: number; crf?: number; audioBitrate?: string } = {}
): Promise<void> {
  const { width = 1920, height = 1080, crf = 23, audioBitrate = "128k" } = options;

  return new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .videoCodec("libx264")
      .audioCodec("aac")
      .audioBitrate(audioBitrate)
      .outputOptions([
        `-crf ${crf}`,          // Quality: 0 (lossless) - 51 (worst); 23 = default, 18 = high quality
        "-preset fast",          // Encoding speed vs compression ratio
        "-movflags +faststart",  // Enable progressive streaming (moov atom at start)
        `-vf scale=${width}:${height}:force_original_aspect_ratio=decrease,pad=${width}:${height}:(ow-iw)/2:(oh-ih)/2`,
      ])
      .output(outputPath)
      .on("end", () => resolve())
      .on("error", (err) => reject(new Error(`Transcode failed: ${err.message}`)))
      .run();
  });
}
```

## HLS (HTTP Live Streaming) Packaging
```ts
// src/lib/hls.ts — Generate HLS manifest + segments for adaptive streaming
export async function generateHLS(inputPath: string, outputDir: string): Promise<string> {
  await fs.mkdir(outputDir, { recursive: true });
  const playlistPath = path.join(outputDir, "playlist.m3u8");

  return new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .outputOptions([
        "-codec: copy",              // Copy codecs when input is already H.264/AAC
        "-start_number 0",
        "-hls_time 6",               // Segment duration (seconds)
        "-hls_list_size 0",          // Keep all segments in playlist (0 = unlimited)
        "-hls_segment_filename", path.join(outputDir, "segment_%03d.ts"),
        "-f hls",
      ])
      .output(playlistPath)
      .on("end", () => resolve(playlistPath))
      .on("error", (err) => reject(err))
      .run();
  });
}

// Multi-bitrate HLS (adaptive streaming)
export async function generateAdaptiveHLS(inputPath: string, outputDir: string): Promise<string> {
  await fs.mkdir(outputDir, { recursive: true });

  // Transcode to multiple quality levels in parallel
  const renditions = [
    { height: 360, bitrate: "800k", name: "360p" },
    { height: 720, bitrate: "2500k", name: "720p" },
    { height: 1080, bitrate: "5000k", name: "1080p" },
  ];

  await Promise.all(renditions.map(async (r) => {
    const renditionDir = path.join(outputDir, r.name);
    await fs.mkdir(renditionDir, { recursive: true });
    await new Promise<void>((resolve, reject) => {
      ffmpeg(inputPath)
        .videoCodec("libx264")
        .audioCodec("aac")
        .audioBitrate("128k")
        .outputOptions([
          `-vf scale=-2:${r.height}`,
          `-b:v ${r.bitrate}`,
          `-maxrate ${r.bitrate}`,
          `-bufsize ${parseInt(r.bitrate) * 2}k`,
          "-hls_time 6",
          "-hls_list_size 0",
          `-hls_segment_filename ${path.join(renditionDir, "segment_%03d.ts")}`,
          "-f hls",
        ])
        .output(path.join(renditionDir, "playlist.m3u8"))
        .on("end", () => resolve())
        .on("error", reject)
        .run();
    });
  }));

  // Generate master playlist
  const masterPlaylist = renditions
    .map((r) => `#EXT-X-STREAM-INF:BANDWIDTH=${parseInt(r.bitrate) * 1000},RESOLUTION=x${r.height}\n${r.name}/playlist.m3u8`)
    .join("\n");
  const masterPath = path.join(outputDir, "master.m3u8");
  await fs.writeFile(masterPath, `#EXTM3U\n${masterPlaylist}`);
  return masterPath;
}
```

## Video Thumbnail Generation
```ts
// src/lib/thumbnail.ts
export function extractVideoFrame(
  inputPath: string,
  outputPath: string,
  options: { time?: string; width?: number } = {}
): Promise<void> {
  const { time = "00:00:05", width = 640 } = options;

  return new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .screenshots({
        timestamps: [time],
        filename: path.basename(outputPath),
        folder: path.dirname(outputPath),
        size: `${width}x?`,  // Maintain aspect ratio
      })
      .on("end", () => resolve())
      .on("error", (err) => reject(new Error(`Thumbnail failed: ${err.message}`)));
  });
}

// Generate multiple thumbnails for video preview
export async function generatePreviewGrid(
  inputPath: string,
  outputDir: string,
  count = 9
): Promise<string[]> {
  const probe = await probeMedia(inputPath);
  const duration = probe.format.duration ?? 0;
  const interval = duration / (count + 1);

  const timestamps = Array.from({ length: count }, (_, i) =>
    ((i + 1) * interval).toFixed(2)
  );

  return new Promise((resolve, reject) => {
    const outputFiles: string[] = [];
    ffmpeg(inputPath)
      .screenshots({ timestamps, folder: outputDir, size: "320x?" })
      .on("filenames", (filenames) => outputFiles.push(...filenames.map((f) => path.join(outputDir, f))))
      .on("end", () => resolve(outputFiles))
      .on("error", reject);
  });
}
```

## Audio Processing
```ts
// Normalize audio loudness to -14 LUFS (streaming platform standard)
export function normalizeAudio(inputPath: string, outputPath: string): Promise<void> {
  return new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .audioFilter("loudnorm=I=-14:TP=-1.5:LRA=11")  // EBU R128 normalization
      .audioCodec("libmp3lame")
      .audioBitrate("192k")
      .output(outputPath)
      .on("end", () => resolve())
      .on("error", reject)
      .run();
  });
}

// Extract audio from video
export function extractAudio(inputPath: string, outputPath: string): Promise<void> {
  return new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .noVideo()
      .audioCodec("libmp3lame")
      .audioBitrate("128k")
      .output(outputPath)
      .on("end", () => resolve())
      .on("error", reject)
      .run();
  });
}
```

## BullMQ Job Queue Integration
```ts
// src/jobs/video-transcode.ts — async processing
import { Queue, Worker } from "bullmq";
import { redis } from "@/lib/redis";
import { transcodeToMp4, generateHLS } from "@/lib/ffmpeg";

const QUEUE_NAME = "video-transcode";
export const transcodeQueue = new Queue(QUEUE_NAME, { connection: redis });

interface TranscodeJob {
  inputPath: string;
  outputDir: string;
  userId: string;
  mediaId: string;
}

export const transcodeWorker = new Worker<TranscodeJob>(
  QUEUE_NAME,
  async (job) => {
    const { inputPath, outputDir, mediaId } = job.data;
    try {
      await job.updateProgress(10);
      await transcodeToMp4(inputPath, `${outputDir}/video.mp4`);
      await job.updateProgress(60);
      const playlistPath = await generateHLS(`${outputDir}/video.mp4`, `${outputDir}/hls`);
      await job.updateProgress(90);
      await db.media.update({ where: { id: mediaId }, data: { status: "ready", playlistPath } });
      await job.updateProgress(100);
    } finally {
      // Always cleanup temp input file
      await fs.unlink(inputPath).catch(() => {});
    }
  },
  { connection: redis, concurrency: 2 }
);

// API endpoint to enqueue job
// POST /api/media/:id/transcode → transcodeQueue.add("transcode", jobData)
```

## MIME Type Security Validation
```ts
import { magic } from "file-type";  // Uses file magic bytes, not extension

export async function validateMediaFile(filePath: string): Promise<{ mimeType: string; valid: boolean }> {
  const type = await magic.fromFile(filePath);
  const allowed = ["video/mp4", "video/webm", "video/quicktime", "audio/mpeg", "audio/wav"];
  return { mimeType: type?.mime ?? "unknown", valid: allowed.includes(type?.mime ?? "") };
}
```

## Common FFmpeg Commands (raw)
```bash
# Basic MP4 transcode
ffmpeg -i input.avi -c:v libx264 -crf 23 -preset fast -c:a aac -b:a 128k -movflags +faststart output.mp4

# WebM (VP9 + Opus) — better compression for web
ffmpeg -i input.mp4 -c:v libvpx-vp9 -crf 30 -b:v 0 -c:a libopus -b:a 128k output.webm

# HLS segmentation
ffmpeg -i input.mp4 -codec: copy -hls_time 6 -hls_list_size 0 -f hls output.m3u8

# Extract frame at 5 seconds
ffmpeg -ss 5 -i input.mp4 -frames:v 1 -vf scale=640:-1 thumbnail.jpg

# Audio normalization (EBU R128)
ffmpeg -i input.mp3 -af "loudnorm=I=-14:TP=-1.5:LRA=11" -c:a libmp3lame -b:a 192k output.mp3
```
