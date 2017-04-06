package Final;

/**
 * Created by Герман on 25.03.2017.
 */
public class Offer {
    private int id;
    private int nShares;
    private int price;
    private User user;
    private String societyName;

    public Offer(int id, int nShares, int price, String societyName, User user) {
        this.id = id;
        this.nShares = nShares;
        this.price = price;
        this.societyName = societyName;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public int getnShares() {
        return nShares;
    }

    public int getPrice() {
        return price;
    }

    public String getSocietyName() {
        return societyName;
    }

    public String getDealerName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setnShares(int nShares) {
        this.nShares = nShares;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public void setDealer(String dealer) {
        this.user = user;
    }

    @Override
    public String toString() {

        return "id= " + id + "\tdealer_name= " + user.getName() + "\tquantity= " + nShares + "\tprice= " + price + "\tsociety_name= " + societyName;
    }
}
