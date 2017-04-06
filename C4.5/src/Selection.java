/**
 * Created by Flynn on 04/04/2017.
 */
public class Selection {
    private String type;
    private String lifeStyle;
    private double vacation;
    private double eCredit;
    private double salary;
    private double property;
    private String label;

    public Selection(String type, String lifeStyle, double vacation, double eCredit, double salary,
                     double property, String label) {
        this.type = type;
        this.lifeStyle = lifeStyle;
        this.vacation = vacation;
        this.eCredit = eCredit;
        this.salary = salary;
        this.property = property;
        this.label = label;
    }

    public Selection() {

    }

    public double geteCredit() {
        return eCredit;
    }
    public double getProperty() {
        return property;
    }
    public double getSalary() {
        return salary;
    }
    public double getVacation() {
        return vacation;
    }
    public String getLabel() {
        return label;
    }
    public String getLifeStyle() {
        return lifeStyle;
    }
    public String getType() {
        return type;
    }

    public void seteCredit(double eCredit) {
        this.eCredit = eCredit;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setLifeStyle(String lifeStyle) {
        this.lifeStyle = lifeStyle;
    }
    public void setProperty(double property) {
        this.property = property;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setVacation(double vacation) {
        this.vacation = vacation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(type)
                .append(", Life Style: ").append(lifeStyle)
                .append(", Vacation: ").append(vacation)
                .append(", eCredit: ").append(eCredit)
                .append(", Salary: ").append(salary)
                .append(", Property: ").append(property)
                .append(", Label: ").append(label);
        return sb.toString();
    }
}
