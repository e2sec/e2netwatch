import { UserProfilePage } from './user-profile.po';

describe('User profile page', () => {
    let userProfilePage: UserProfilePage;

    beforeEach(() => {
        userProfilePage = new UserProfilePage();
    });

    it('should load User profile page', () => {
        userProfilePage.navigateTo();
        expect(userProfilePage.getTitle()).toEqual('Profile');
    });
});
