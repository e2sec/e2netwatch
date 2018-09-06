import { trigger, state, style, transition, animate } from '@angular/animations';

export const Animations = {
    slideInOutSidebar: trigger('slideInOutSidebar', [
        state('collapsed', style({
            width: '46px',
            overflow: 'hidden'
        })),
        state('extended', style({
            width: '250px'
        })),
        transition('* => *', animate('800ms ease-in-out')),
    ]),
    slideInOutMainContent: trigger('slideInOutMain', [
        state('collapsed', style({
            marginLeft: '46px',
        })),
        state('extended', style({
            marginLeft: '250px',
        })),
        transition('* => *', animate('800ms ease-in-out')),
    ])
};
