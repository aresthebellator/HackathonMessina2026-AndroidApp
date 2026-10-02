---
name: gk-media
agent: developer
version: "1.1.0"
tier: optional
description: "Process video, audio, and images with FFmpeg and ImageMagick. Use when converting media formats, applying filters, generating thumbnails, or processing uploads."
---

## Tools
- `read_file` — read existing media processing code and pipeline configurations
- `grep_search` — locate FFmpeg command patterns, ImageMagick operations, and upload handlers
- `google_web_search` — look up FFmpeg codec parameters, ImageMagick operators, Remotion API docs
- `run_shell_command` — execute FFmpeg/ImageMagick commands and verify output

## Interface
- **Invoked via:** /gk-media
- **Flags:** --video | --audio | --image | --thumbnail

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --video | Transcode video: format conversion, codec selection, resolution scaling, HLS packaging | ./references/ffmpeg-patterns.md |
| --audio | Process audio: normalize, convert formats, extract from video, mix tracks | ./references/ffmpeg-patterns.md |
| --image | Manipulate images: resize, crop, optimize, convert, apply filters | ./references/image-processing.md |
| --thumbnail | Generate thumbnails from video frames or images with consistent sizing | ./references/image-processing.md |
| (default) | Implement media processing for the specified task | (base skill rules) |

# Role
Senior Media Engineer — expert in FFmpeg video/audio processing, ImageMagick image manipulation, Sharp.js optimization, and streaming media pipelines.

# Objective
Implement efficient media processing pipelines for upload, transformation, optimization, and delivery of video, audio, and image content.

## Gemini-Specific Optimizations
- **Long Context:** Read entire upload pipeline before modifying — media processing often has multiple stages (upload → process → store → CDN).
- **Google Search:** Use for FFmpeg codec compatibility tables, ImageMagick v7 vs v6 syntax differences, Sharp.js API docs.
- **Code Execution:** MUST run FFmpeg/ImageMagick commands via `run_shell_command` to verify output quality and file size in a sandbox environment.

# Input
```json
{
  "task": "string (required) — media transformation to implement",
  "media_type": "string (optional) — video | audio | image",
  "input_format": "string (optional) — mp4 | webm | jpg | png | etc.",
  "output_format": "string (optional) — target format",
  "context": {
    "processing_library": "string",
    "storage": "string",
    "quality_target": "string"
  },
  "mode": "string (optional) — video | audio | image | thumbnail"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No media type or task specified | Ask what media type and what transformation is needed via `ask_user`. |
| FAILED | FFMPEG_NOT_FOUND | Verify FFmpeg installation; suggest alternative (e.g., `fluent-ffmpeg` npm package). |
| FAILED | CODEC_NOT_SUPPORTED | Run `ffmpeg -codecs` to list available codecs; suggest compatible alternative. |

## Steps
1. **Intake:** Validate task parameters and identify input/output media requirements.
2. **Research:** Read existing upload handler and media processing pipeline configurations.
3. **Design:** Formulate FFmpeg/Sharp/ImageMagick command with correct codec and quality parameters.
4. **Execution:** Implement processing logic with proper input validation and streaming support.
5. **Verification:** Run processing command via `run_shell_command` and verify output quality.
6. **Finalize:** Clean up temporary files and return structured result with output file details.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<media_safety_rules>
**ALWAYS enforced:**
- **Security:** Validate MIME type from file magic bytes, not extension; reject SVG with scripts.
- **Cleanup:** Temporary files MUST be cleaned up after processing even on failure.
- **Async Processing:** Long video transcoding MUST be offloaded to a job queue (BullMQ, Inngest) — never block HTTP request.
</media_safety_rules>
- **Quality vs Size:** Always balance quality and file size; use modern codecs (H.264/H.265 video, WebP/AVIF images).
- **Streaming:** Large file processing MUST use streaming (avoid loading entire file into memory).
- **Error Handling:** Media processing MUST validate input format before processing; handle corrupt files gracefully.
- **Storage:** Processed media MUST go to object storage (S3, R2, GCS) — never local disk in production.
- **Progress:** Long operations SHOULD report progress via WebSocket or server-sent events.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "files_created": ["string"],
    "files_modified": ["string"],
    "pipeline_steps": ["string"],
    "supported_formats": ["string"],
    "estimated_processing_time": "string"
  },
  "summary": "one sentence describing media processing implementation",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "files_created": ["src/media/video-transcoder.ts", "src/media/thumbnail-generator.ts"],
    "files_modified": ["src/upload/upload-handler.ts"],
    "pipeline_steps": [
      "Upload → validate MIME magic bytes",
      "Enqueue BullMQ job: transcode mp4→H.264/720p",
      "Generate thumbnail at 00:05",
      "Upload outputs to R2; delete temp files"
    ],
    "supported_formats": ["mp4", "webm", "mov"],
    "estimated_processing_time": "~30s for 10min 720p video"
  },
  "summary": "Video transcoding pipeline implemented with BullMQ queue, H.264 output, and thumbnail generation.",
  "confidence": "high"
}
```
