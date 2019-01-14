package pz.services.settings.language;

public enum Bundles {
    UTILS_DIALOG_ERROR("utils.dialog.error"),
    UTILS_DIALOG_WARNING("utils.dialog.warning"),
    UTILS_DIALOG_SUCCESS("utils.dialog.success"),

    LOGIN_HEADER("login.header"),
    LOGIN_LOGIN_LABEL("login.login-label"),
    LOGIN_PASSWORD_LABEL("login.password-label"),
    LOGIN_PERFORM_LOGIN_BUTTON("login.perform-login-button"),
    LOGIN_SETTINGS_BUTTON("login.settings-button"),
    LOGIN_DIALOG_AUTHENTICATION_ERROR_HEADER("login.dialog.authentication-error.header"),
    LOGIN_DIALOG_AUTHENTICATION_ERROR_TITLE("login.dialog.authentication-error.title"),
    LOGIN_ERROR_INVALID_USER_NAME_OR_PASSWORD("login.error.invalid-user-name-or-password"),

    SETTINGS_HEADER("settings.header"),
    SETTINGS_LANGUAGE_LABEL("settings.language-label"),
    SETTINGS_SKIN_LABEL("settings.skin-label"),
    SETTINGS_APPLY_BUTTON("settings.apply-button"),

    APARTMENT_SELECT_APARTMENT_SELECT_BUTTON("apartment-select.apartment-select-button"),
    APARTMENT_SELECT_CAPACITY_LABEL("apartment-select.capacity-label"),
    APARTMENT_SELECT_COLUMN_CAPACITY("apartment-select.column.capacity"),
    APARTMENT_SELECT_COLUMN_COST("apartment-select.column.cost"),
    APARTMENT_SELECT_COLUMN_FLOOR("apartment-select.column.floor"),
    APARTMENT_SELECT_COLUMN_NUMBER("apartment-select.column.number"),
    APARTMENT_SELECT_DIALOG_BOOKING_SUCCESS_CONTENT("apartment-select.dialog.booking-success.content"),
    APARTMENT_SELECT_DIALOG_BOOKING_SUCCESS_HEADER("apartment-select.dialog.booking-success.header"),
    APARTMENT_SELECT_DIALOG_INVALID_FORM_CONTENT("apartment-select.dialog.invalid-form.content"),
    APARTMENT_SELECT_DIALOG_INVALID_FORM_DEFAULT_APARTMENT_NUMBER("apartment-select.dialog.invalid-form.default-apartment-number"),
    APARTMENT_SELECT_DIALOG_INVALID_FORM_HEADER("apartment-select.dialog.invalid-form.header"),
    APARTMENT_SELECT_END_DATE_LABEL("apartment-select.end-date-label"),
    APARTMENT_SELECT_GO_BACK_BUTTON("apartment-select.go-back-button"),
    APARTMENT_SELECT_SHOW_HOTEL_MAP_BUTTON("apartment-select.show-hotel-map-button"),
    APARTMENT_SELECT_START_DATE_LABEL("apartment-select.start-date-label"),
    APARTMENT_SELECT_TOTAL_COST_LABEL("apartment-select.total-cost-label"),

    CLIENT_FORM_ADDRESS_LABEL("client-form.address-label"),
    CLIENT_FORM_CANCEL_BUTTON("client-form.cancel-button"),
    CLIENT_FORM_CONFIRM_BUTTON("client-form.confirm-button"),
    CLIENT_FORM_CITY_LABEL("client-form.city-label"),
    CLIENT_FORM_DIALOG_CLIENT_ACTION_CREATED("client-form.dialog.client-action.created"),
    CLIENT_FORM_DIALOG_CLIENT_ACTION_EDITED("client-form.dialog.client-action.edited"),
    CLIENT_FORM_DIALOG_EMAIL_TAKEN_CONTENT("client-form.dialog.email-taken.content"),
    CLIENT_FORM_DIALOG_EMAIL_TAKEN_HEADER("client-form.dialog.email-taken.header"),
    CLIENT_FORM_DIALOG_MANDATORY_FIELDS_NOT_FILLED_HEADER("client-form.dialog.mandatory-fields-not-filled.header"),
    CLIENT_FORM_DIALOG_MANDATORY_FIELDS_NOT_FILLED_CONTENT("client-form.dialog.mandatory-fields-not-filled.content"),
    CLIENT_FORM_DIALOG_SUCCESS_CONTENT("client-form.dialog.success.content"),
    CLIENT_FORM_EMAIL_LABEL("client-form.email-label"),
    CLIENT_FORM_NAME_LABEL("client-form.name-label"),
    CLIENT_FORM_POSTAL_CODE_LABEL("client-form.postal-code-label"),
    CLIENT_FORM_SURNAME_LABEL("client-form.surname-label"),

    MANAGE_CLIENT_ADD_CLIENT_BUTTON("manage-client.add-client-button"),
    MANAGE_CLIENT_CLIENT_SELECT_BUTTON("manage-client.client-select-button"),
    MANAGE_CLIENT_CLOSE_BUTTON("manage-client.close-button"),
    MANAGE_CLIENT_COLUMN_ADDRESS("manage-client.column.address"),
    MANAGE_CLIENT_COLUMN_CITY("manage-client.column.city"),
    MANAGE_CLIENT_COLUMN_EMAIL("manage-client.column.email"),
    MANAGE_CLIENT_COLUMN_NAME("manage-client.column.name"),
    MANAGE_CLIENT_COLUMN_POSTAL_CODE("manage-client.column.postal-code"),
    MANAGE_CLIENT_COLUMN_SURNAME("manage-client.column.surname"),
    MANAGE_CLIENT_DELETE_CLIENT_BUTTON("manage-client.delete-client-button"),
    MANAGE_CLIENT_DIALOG_CLIENT_DELETE_CONTENT("manage-client.dialog.client-delete.content"),
    MANAGE_CLIENT_DIALOG_CLIENT_DELETE_HEADER("manage-client.dialog.client-delete.header"),
    MANAGE_CLIENT_DIALOG_CLIENT_DELETE_SUCCESS_CONTENT("manage-client.dialog.client-delete.success.content"),
    MANAGE_CLIENT_DIALOG_CLIENT_DELETE_TITLE("manage-client.dialog.client-delete.title"),
    MANAGE_CLIENT_DIALOG_CLIENT_NOT_SELECTED_CONTENT("manage-client.dialog.client-not-selected.content"),
    MANAGE_CLIENT_DIALOG_CLIENT_NOT_SELECTED_HEADER("manage-client.dialog.client-not-selected.header"),
    MANAGE_CLIENT_EDIT_CLIENT_BUTTON("manage-client.edit-client-button"),
    MANAGE_CLIENT_SEARCH_TEXT_FIELD_PROMPT("manage-client.search-text-field-prompt"),

    MENU_BOOK_APARTMENT_BUTTON("menu.book-apartment-button"),
    MENU_MANAGE_BOOKINGS_BUTTON("menu.manage-bookings-button"),
    MENU_MANAGE_CLIENTS_BUTTON("menu.manage-clients-button"),
    MENU_QUIT_BUTTON("menu.quit-button"),
    MENU_SETTINGS_BUTTON("menu.settings-button"),
    MENU_SHOW_HOTEL_MAP_BUTTON("menu.show-hotel-map-button"),

    ;
    public String value;

    Bundles(String value) {
        this.value = value;
    }
}
