package in.straightbuy.www.straightbuy;

public class NewUser {

    private String nameOfUser, contact , email, username, password;

    public NewUser(){

    }

    public NewUser(String nameOfUser, String contact, String email, String username, String password) {
        this.nameOfUser = nameOfUser;
        this.contact = contact;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
