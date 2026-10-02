import React, { useState, useEffect, useRef } from 'react';
import { UnitBanner } from './UnitBanner';
import { PathNode } from './PathNode';
import { LessonPreviewModal } from './LessonPreviewModal';
import { UNITS_DATA, getSerpentineOffset, getUnitForLesson } from '@/lib/unitsData';
import { useQuizStore } from '@/store/useQuizStore';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { Unit } from '@/types';

export const DuolingoPath: React.FC = () => {
  const currentLessonIndex = useQuizStore((s) => s.currentLessonIndex);
  const completedLessons = useQuizStore((s) => s.completedLessons);
  const lessonStars = useQuizStore((s) => s.lessonStars);
  const { loadLesson, isLoading } = useWikipediaQuiz();

  const [selectedLesson, setSelectedLesson] = useState<number | null>(null);
  const [selectedUnit, setSelectedUnit] = useState<Unit | null>(null);
  const [selectedStatus, setSelectedStatus] = useState<'completed' | 'current' | 'locked'>('locked');

  const currentNodeRef = useRef<HTMLDivElement | null>(null);

  // Auto-scroll gently to current lesson node on load
  useEffect(() => {
    if (currentNodeRef.current) {
      setTimeout(() => {
        currentNodeRef.current?.scrollIntoView({
          behavior: 'smooth',
          block: 'center',
        });
      }, 300);
    }
  }, []);

  const handleNodeClick = (lessonNumber: number, status: 'completed' | 'current' | 'locked') => {
    const unit = getUnitForLesson(lessonNumber);
    setSelectedLesson(lessonNumber);
    setSelectedUnit(unit);
    setSelectedStatus(status);
  };

  const handleStartLesson = (lessonNumber: number) => {
    loadLesson(lessonNumber, 5);
    setSelectedLesson(null);
  };

  return (
    <div className="w-full max-w-lg mx-auto px-4 pb-24 pt-4 relative flex flex-col items-center">
      {UNITS_DATA.map((unit) => {
        // Generate array of 10 lessons for this unit
        const lessonNumbers: number[] = [];
        for (let i = unit.startLesson; i <= unit.endLesson; i++) {
          lessonNumbers.push(i);
        }

        return (
          <section key={unit.id} className="w-full my-6 flex flex-col items-center">
            {/* Unit Section Header Banner (Every 10 lessons) */}
            <UnitBanner
              unit={unit}
              completedCount={
                completedLessons.filter((l) => l >= unit.startLesson && l <= unit.endLesson).length
              }
            />

            {/* Path Nodes container */}
            <div className="relative w-full flex flex-col items-center my-4">
              {/* Vertical connecting curved dashed line */}
              <svg
                className="absolute top-0 bottom-0 left-1/2 -translate-x-1/2 w-48 h-full pointer-events-none opacity-20 stroke-gray-400 -z-0"
                fill="none"
              >
                <path
                  d="M 96 0 Q 60 80, 96 160 T 96 320 T 96 480 T 96 640 T 96 800 T 96 960"
                  strokeWidth="6"
                  strokeDasharray="8 8"
                />
              </svg>

              {lessonNumbers.map((lNum, idx) => {
                const lessonIndexInUnit = idx + 1; // 1 to 10
                const offsetPx = getSerpentineOffset(lessonIndexInUnit);
                const isCompleted = completedLessons.includes(lNum);
                const isCurrent = lNum === currentLessonIndex;
                const status: 'completed' | 'current' | 'locked' = isCompleted
                  ? 'completed'
                  : isCurrent
                  ? 'current'
                  : 'locked';
                const stars = lessonStars[lNum] || 0;
                const isCheckpoint = lNum % 10 === 0;

                return (
                  <div
                    key={lNum}
                    ref={isCurrent ? currentNodeRef : null}
                    className="relative z-10 w-full flex justify-center"
                  >
                    <PathNode
                      lessonNumber={lNum}
                      status={status}
                      stars={stars}
                      isCheckpoint={isCheckpoint}
                      offsetPx={offsetPx}
                      unit={unit}
                      onClick={() => handleNodeClick(lNum, status)}
                    />
                  </div>
                );
              })}
            </div>
          </section>
        );
      })}

      {/* Lesson Details & Start Modal */}
      <LessonPreviewModal
        lessonNumber={selectedLesson}
        unit={selectedUnit}
        status={selectedStatus}
        stars={selectedLesson ? lessonStars[selectedLesson] : 0}
        isLoading={isLoading}
        onClose={() => setSelectedLesson(null)}
        onStart={handleStartLesson}
      />
    </div>
  );
};
