package basil.sanity.Controllers;

/**
 * Created by Utsav on 10/7/2017.
 */

public class User {
    private String email;
    private String username;
    private String hashPassword;
    private int questionInt;
    private String hashSecurityAns;

    public User(String email, String username, String hashPassword) {
        this.email = email;
        this.username = username;
        this.hashPassword = hashPassword;
        this.questionInt = 0;
        this.hashSecurityAns = "";
    }

    public User(String email, String username, String hashPassword,
                int questionInt, String hashSecurityAns) {
        this.email = email;
        this.username = username;
        this.hashPassword = hashPassword;
        this.questionInt = questionInt;
        this.hashSecurityAns = hashSecurityAns;
    }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getHash() {
        return hashPassword;
    }

    public int getQuestionInt() { return questionInt;}

    public String getHashSecurityAns() {
        return hashSecurityAns;
    }

    @Override
    public boolean equals(Object u) {
        return email.equals(((User)u).email) && username.equals(((User)u).username) &&
                hashPassword.equals(((User)u).hashPassword);
    }
}
