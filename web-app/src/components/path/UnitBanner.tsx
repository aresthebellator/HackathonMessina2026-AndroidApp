import React from 'react';
import { Landmark, Rocket, Palette, Compass, Lightbulb, BookOpen, BookMarked } from 'lucide-react';
import { Unit } from '@/types';

interface UnitBannerProps {
  unit: Unit;
  onOpenGuide?: () => void;
}

const ICON_MAP: Record<string, React.FC<{ className?: string }>> = {
  Landmark,
  Rocket,
  Palette,
  Compass,
  Lightbulb,
  BookOpen,
};

export const UnitBanner: React.FC<UnitBannerProps> = ({ unit, onOpenGuide }) => {
  const IconComponent = ICON_MAP[unit.iconName] || BookOpen;

  return (
    <div
      className="w-full rounded-3xl p-5 sm:p-6 text-white shadow-md relative overflow-hidden mb-8 border-b-4 select-none"
      style={{
        backgroundColor: unit.theme.primary,
        borderColor: unit.theme.dark,
      }}
    >
      {/* Background subtle watermark icon */}
      <div className="absolute -right-4 -bottom-6 opacity-15 pointer-events-none">
        <IconComponent className="w-36 h-36" />
      </div>

      <div className="relative z-10 flex items-start justify-between gap-4">
        <div className="space-y-1 max-w-md">
          <div className="flex items-center gap-2">
            <span className="text-xs font-black uppercase tracking-wider bg-black/20 px-2.5 py-1 rounded-xl">
              {unit.title} • {unit.subtitle}
            </span>
          </div>
          <h2 className="text-xl sm:text-2xl font-black tracking-tight pt-1">
            {unit.topic}
          </h2>
          <p className="text-xs sm:text-sm font-semibold opacity-90 leading-relaxed pt-0.5">
            {unit.description}
          </p>
        </div>

        {/* Guidebook Button */}
        {onOpenGuide && (
          <button
            onClick={onOpenGuide}
            className="shrink-0 p-3 rounded-2xl bg-white/20 hover:bg-white/30 active:scale-95 transition-all text-white border border-white/30 flex items-center gap-1.5 text-xs font-black uppercase tracking-wider backdrop-blur-xs"
            title="Guida della sezione"
          >
            <BookMarked className="w-4 h-4" />
            <span className="hidden sm:inline">Guida</span>
          </button>
        )}
      </div>
    </div>
  );
};
