package za.co.wethinkcode.weshare.app.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class ExpenseViewModel {
    private String dueDate;
    private String description;
    private String paidBy;
    private String id;
    private Double amount;

    public ExpenseViewModel(String dueDate, String description, String paidBy, String id, Double amount) {
        this.amount = amount;
        this.id = id;
        this.paidBy = paidBy;
        this.description = description;
        this.dueDate = dueDate;
    }

    public LocalDate dueDateAsLocalDate() {
        try {
            return LocalDate.parse(this.dueDate);
        } catch (DateTimeException e) {
            throw new RuntimeException("Could not parse dueDate for expense: [" + this.dueDate + "]");
        }
    }

    public Double getExpenseAmount() { return this.amount; }

    public void setExpenseAmount(Double amount) { this.amount = amount; }

    public String getExpensePaidBy() { return this.paidBy; }

    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
