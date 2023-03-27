export default class UpdateUserCommand {

    private readonly newUsername: string;
    private readonly newEmail: string;

    private readonly newPassword: string;

    private readonly newVisibility: string;
    private readonly newLanguages: Array<string>;
    private readonly newDescription: string;

    private readonly currentPassword: string;

    constructor(newUsername: string, newEmail: string, newPassword: string, newVisibility: string, newLanguages: Array<string>, newDescription: string, currentPassword: string) {
        this.newUsername = newUsername;
        this.newEmail = newEmail;

        this.newPassword = newPassword;

        this.newVisibility = newVisibility;
        this.newLanguages = newLanguages;
        this.newDescription = newDescription;
        
        this.currentPassword = currentPassword;
    }
}
