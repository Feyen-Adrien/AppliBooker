package src.Helpers;

public class RequestBuilder {

    public static String buildLoginRequest(String user, String password) {
        return String.format("LOGIN#%s#%s", user, password);
    }
    public static String buildLogoutRequest() {
        return "LOGOUT#";
    }

    public static String buildGetAuthorsRequest() {
        return "GET_AUTHORS#";
    }
    public static String buildGetSubjectsRequest() {
        return "GET_SUBJECTS#";
    }
    public static String buildGetBooksRequest() {
        return "GET_BOOKS#";
    }

    public static String buildAddBookRequest(String title, String ISBN, int pages, double price, int publishyear, int stock, String author, String subject) {
        return String.format("ADD_BOOK#%s#%s#%d#%f#%d#%d#%s#%s", title, ISBN, pages, price, publishyear, stock, author, subject);
    }
    public static String buildAddAuthorRequest(String nom, String prenom, String date) {
        return String.format("ADD_AUTHOR#%s#%s#%s", nom, prenom, date);
    }
    public static String buildAddSubjectRequest(String subject) {
        return String.format("ADD_SUBJECT#%s", subject);
    }

}
