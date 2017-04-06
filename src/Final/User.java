package Final;

/**
 * Created by Герман on 25.03.2017.
 */
public class User {
    private String name;
    private String password;
    private int sharesFootballSociety;
    private int sharesTennisSociety;
    private int sharesChessSociety;
    private int balance;

    public User(String name, String password, int sharesFootballSociety, int sharesTennisSociety, int sharesChessSociety, int balance) {
        this.name = name;
        this.password = password;
        this.sharesFootballSociety = sharesFootballSociety;
        this.sharesTennisSociety = sharesTennisSociety;
        this.sharesChessSociety = sharesChessSociety;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getSharesFootballSociety() {
        return sharesFootballSociety;
    }

    public int getSharesTennisSociety() {
        return sharesTennisSociety;
    }

    public int getSharesChessSociety() {
        return sharesChessSociety;
    }

    public int getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSharesFootballSociety(int sharesFootballSociety) {
        this.sharesFootballSociety = sharesFootballSociety;
    }

    public void setSharesTennisSociety(int sharesTennisSociety) {
        this.sharesTennisSociety = sharesTennisSociety;
    }

    public void setSharesChessSociety(int sharesChessSociety) {
        this.sharesChessSociety = sharesChessSociety;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
