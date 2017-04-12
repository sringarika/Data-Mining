/**
 * Created by Flynn on 04/04/2017.
 */
public class Introduction {
    private String serviceType;
    private String customer;
    private double monthlyFee;
    private double advertisementBudget;
    private String size;
    private String promotion;
    private double interestRate;
    private double period;
    private String label;

    public Introduction() {

    }

    public Introduction(String serviceType, String customer, double monthlyFee, double advertisementBudget,
                        String size, String promotion, double interestRate, double period, String label) {
        this.serviceType = serviceType;
        this.customer = customer;
        this.monthlyFee = monthlyFee;
        this.advertisementBudget = advertisementBudget;
        this.size = size;
        this.promotion = promotion;
        this.interestRate = interestRate;
        this.period = period;
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public double getAdvertisementBudget() {
        return advertisementBudget;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public double getPeriod() {
        return period;
    }

    public String getCustomer() {
        return customer;
    }

    public String getPromotion() {
        return promotion;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getSize() {
        return size;
    }

    public void setAdvertisementBudget(double advertisementBudget) {
        this.advertisementBudget = advertisementBudget;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
