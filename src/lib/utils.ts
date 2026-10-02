import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Fisher-Yates shuffle algorithm for impartial option randomization
 */
export function shuffleArray<T>(array: T[]): T[] {
  const result = [...array];
  for (let i = result.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [result[i], result[j]] = [result[j], result[i]];
  }
  return result;
}

/**
 * Format today's date in YYYY-MM-DD
 */
export function getTodayDateString(): string {
  const now = new Date();
  return now.toISOString().split('T')[0];
}

/**
 * Check if date string was yesterday
 */
export function isYesterday(dateStr: string): boolean {
  if (!dateStr) return false;
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const prev = new Date(today);
  prev.setDate(prev.getDate() - 1);

  const target = new Date(dateStr);
  target.setHours(0, 0, 0, 0);

  return target.getTime() === prev.getTime();
}

/**
 * Strip HTML tags from string if any
 */
export function stripHtml(html: string): string {
  return html.replace(/<[^>]*>?/gm, '');
}

/**
 * Truncate long text cleanly at word boundaries
 */
export function truncateWords(text: string, maxChars: number = 180): string {
  if (!text || text.length <= maxChars) return text;
  const cut = text.substring(0, maxChars);
  const lastSpace = cut.lastIndexOf(' ');
  return (lastSpace > 0 ? cut.substring(0, lastSpace) : cut) + '...';
}
