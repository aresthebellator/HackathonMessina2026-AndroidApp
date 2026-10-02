import React from 'react';
import { cn } from '@/lib/utils';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'green' | 'blue' | 'coral' | 'yellow' | 'outline' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  fullWidth?: boolean;
  children: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  variant = 'green',
  size = 'md',
  fullWidth = false,
  className,
  disabled,
  children,
  ...props
}) => {
  const baseStyles = 'relative inline-flex items-center justify-center font-extrabold uppercase tracking-wider rounded-2xl transition-all select-none cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2';

  const sizeStyles = {
    sm: 'text-xs px-4 py-2 border-b-2 min-h-[38px]',
    md: 'text-sm px-6 py-3 border-b-4 min-h-[48px]',
    lg: 'text-base px-8 py-4 border-b-4 min-h-[56px]',
  };

  const variantStyles = {
    green: disabled
      ? 'bg-[#E5E5E5] text-[#AFAFAF] border-b-0 cursor-not-allowed shadow-none'
      : 'bg-[#58CC02] hover:bg-[#61E002] border-[#46A302] text-white active:translate-y-[2px] active:border-b-2 focus-visible:ring-[#58CC02]',
    blue: disabled
      ? 'bg-[#E5E5E5] text-[#AFAFAF] border-b-0 cursor-not-allowed shadow-none'
      : 'bg-[#1CB0F6] hover:bg-[#20BEFF] border-[#1899D6] text-white active:translate-y-[2px] active:border-b-2 focus-visible:ring-[#1CB0F6]',
    coral: disabled
      ? 'bg-[#E5E5E5] text-[#AFAFAF] border-b-0 cursor-not-allowed shadow-none'
      : 'bg-[#FF4B4B] hover:bg-[#FF5F5F] border-[#EA2B2B] text-white active:translate-y-[2px] active:border-b-2 focus-visible:ring-[#FF4B4B]',
    yellow: disabled
      ? 'bg-[#E5E5E5] text-[#AFAFAF] border-b-0 cursor-not-allowed shadow-none'
      : 'bg-[#FFC800] hover:bg-[#FFD124] border-[#E5A500] text-[#7A5200] active:translate-y-[2px] active:border-b-2 focus-visible:ring-[#FFC800]',
    outline: disabled
      ? 'bg-transparent text-[#AFAFAF] border-2 border-[#E5E5E5] cursor-not-allowed'
      : 'bg-white hover:bg-[#F7F7F7] border-2 border-b-4 border-[#E5E5E5] hover:border-[#D5D5D5] text-[#4B4B4B] active:translate-y-[2px] active:border-b-2 focus-visible:ring-gray-300',
    ghost: 'bg-transparent hover:bg-black/5 text-[#777777] hover:text-[#3C3C3C] border-none active:scale-95',
  };

  return (
    <button
      disabled={disabled}
      className={cn(
        baseStyles,
        sizeStyles[size],
        variantStyles[variant],
        fullWidth ? 'w-full' : '',
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
};
