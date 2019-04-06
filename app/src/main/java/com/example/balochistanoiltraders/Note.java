package com.example.balochistanoiltraders;

public class Note {
    String party_name;
    String brand_name;
    String quantity;
    String rate;
    String concession;
    String special_concession;
    String total_rate;
    String phone_number;
    String job_description;
    String address;
    String employee_name;
    String salary;
    String money_transfer;
    String bank_name;
    String remaining_money;
    String description;
    String spend_money;
    String name;
    String date;
    String loan;
    String money_borrowed;
    String cash;
    String liability;
    String deposite_money;

    public void setCash(String cash) {
        this.cash = cash;
    }

    public void setLiability(String liability) {
        this.liability = liability;
    }

    public void setDeposite_money(String deposite_money) {
        this.deposite_money = deposite_money;
    }

    public String getCash() {
        return cash;
    }

    public String getLiability() {
        return liability;
    }

    public String getDeposite_money() {
        return deposite_money;
    }

    public void setMoney_borrowed(String money_borrowed) {
        this.money_borrowed = money_borrowed;
    }

    public String getMoney_borrowed() {
        return money_borrowed;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getLoan() {
        return loan;
    }

    public void setSpend_money(String spend_money) {
        this.spend_money = spend_money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpend_money() {
        return spend_money;
    }

    public String getName() {
        return name;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public Note(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    String invoice_number
            ;

    public Note() {
    }

    public String getParty_name() {
        return party_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRate() {
        return rate;
    }

    public String getConcession() {
        return concession;
    }

    public String getSpecial_concession() {
        return special_concession;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getJob_description() {
        return job_description;
    }

    public String getAddress() {
        return address;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getSalary() {
        return salary;
    }

    public String getMoney_transfer() {
        return money_transfer;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getRemaining_money() {
        return remaining_money;
    }

    public String getDescription() {
        return description;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setConcession(String concession) {
        this.concession = concession;
    }

    public void setSpecial_concession(String special_concession) {
        this.special_concession = special_concession;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setMoney_transfer(String money_transfer) {
        this.money_transfer = money_transfer;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setRemaining_money(String remaining_money) {
        this.remaining_money = remaining_money;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public Note(String party_name, String brand_name, String quantity,
                String rate, String concession, String special_concession,
                String total_rate, String phone_number, String job_description,
                String address, String employee_name, String salary,
                String money_transfer, String bank_name, String remaining_money,
                String description, String spend_money, String name, String date, String loan,
                String money_borrowed, String liability, String cash, String deposite_money) {
        this.party_name = party_name;
        this.brand_name = brand_name;
        this.quantity = quantity;
        this.rate = rate;
        this.concession = concession;
        this.special_concession = special_concession;
        this.total_rate = total_rate;
        this.phone_number = phone_number;
        this.job_description = job_description;
        this.address = address;
        this.employee_name = employee_name;
        this.salary = salary;
        this.money_transfer = money_transfer;
        this.bank_name = bank_name;
        this.remaining_money = remaining_money;
        this.description = description;
        this.spend_money = spend_money;
        this.name = name;
        this.date = date;
        this.loan = loan;
        this.money_borrowed = money_borrowed;
        this.liability = liability;
        this.cash = cash;
        this.deposite_money = deposite_money;
    }
}
