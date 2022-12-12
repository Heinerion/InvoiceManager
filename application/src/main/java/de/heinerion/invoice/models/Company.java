package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.text.Collator;
import java.util.Objects;

@Getter
@Setter
// public because of CompanyForm
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "company")
public class Company implements Comparable<Company> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "descriptive")
  private String descriptiveName;
  @Column(name = "official")
  private String officialName;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;
  private String signer;
  @Column(name = "tax")
  private String taxNumber;
  @Column(name = "phone")
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @Column(name = "vat")
  private double valueAddedTax;
  @Column(name = "per_hour")
  private double wagesPerHour;

  @Column(name = "invoice_number")
  private int invoiceNumber;

  public Company(String descriptiveName, String officialName, Address address,
                 String signer, String phoneNumber, String taxNumber,
                 double valueAddedTax, double wagesPerHour, Account bankAccount) {
    this.descriptiveName = descriptiveName;
    this.officialName = officialName;
    this.address = address;
    this.signer = signer;
    this.phoneNumber = phoneNumber;
    this.taxNumber = taxNumber;
    this.valueAddedTax = valueAddedTax;
    this.wagesPerHour = wagesPerHour;
    this.account = bankAccount;
  }

  @Override
  public int compareTo(Company company) {
    return Collator.getInstance().compare(this.getDescriptiveName(),
        company.getDescriptiveName());
  }

  public void increaseInvoiceNumber() {
    this.invoiceNumber++;
  }

  @Override
  public String toString() {
    return this.descriptiveName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    if (id != null && id.equals(company.id)) {
      return true;
    }
    return Objects.equals(officialName, company.officialName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descriptiveName);
  }
}
