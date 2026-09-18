/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        duo: {
          green: {
            DEFAULT: '#58CC02',
            dark: '#46A302',
            light: '#D7FFB8',
            subtle: '#F2FCE8',
            text: '#2A7000'
          },
          blue: {
            DEFAULT: '#1CB0F6',
            dark: '#1899D6',
            light: '#DDF4FF',
            subtle: '#F0F9FF',
            text: '#0C70A2'
          },
          yellow: {
            DEFAULT: '#FFC800',
            dark: '#E5A500',
            light: '#FFF5C2',
            subtle: '#FFFDF0',
            text: '#8F6500'
          },
          coral: {
            DEFAULT: '#FF4B4B',
            dark: '#EA2B2B',
            light: '#FFDFE0',
            subtle: '#FFF1F2',
            text: '#B91C1C'
          },
          purple: {
            DEFAULT: '#CE82FF',
            dark: '#A855F7',
            light: '#F3E8FF',
            text: '#7E22CE'
          },
          orange: {
            DEFAULT: '#FF9600',
            dark: '#D97706',
            light: '#FFEDD5',
            text: '#B45309'
          },
          ink: {
            DEFAULT: '#3C3C3C',
            secondary: '#777777',
            light: '#AFAFAF',
            border: '#E5E5E5',
            borderDark: '#CECECE'
          },
          surface: {
            DEFAULT: '#FFFFFF',
            canvas: '#F7F7F7',
            canvasDark: '#131F24',
            surfaceDark: '#1E2D34',
            borderDark: '#37464F'
          }
        }
      },
      boxShadow: {
        'duo-green': '0 4px 0 #46A302',
        'duo-blue': '0 4px 0 #1899D6',
        'duo-yellow': '0 4px 0 #E5A500',
        'duo-coral': '0 4px 0 #EA2B2B',
        'duo-purple': '0 4px 0 #A855F7',
        'duo-gray': '0 4px 0 #E5E5E5',
        'duo-card': '0 2px 0 #E5E5E5',
      },
      fontFamily: {
        sans: ['Nunito', 'Segoe UI', '-apple-system', 'BlinkMacSystemFont', 'system-ui', 'sans-serif'],
      },
      keyframes: {
        'wiggle': {
          '0%, 100%': { transform: 'rotate(-3deg)' },
          '50%': { transform: 'rotate(3deg)' },
        },
        'heart-beat': {
          '0%, 100%': { transform: 'scale(1)' },
          '50%': { transform: 'scale(1.2)' },
        }
      },
      animation: {
        'wiggle': 'wiggle 0.3s ease-in-out infinite',
        'heart-beat': 'heart-beat 0.6s ease-in-out',
      }
    },
  },
  plugins: [],
};
