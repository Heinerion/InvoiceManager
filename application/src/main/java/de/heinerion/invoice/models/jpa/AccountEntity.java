package de.heinerion.betriebe.models.jpa;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class AccountEntity {
  //  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  // Hash Code fixed

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountEntity that = (AccountEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    /*
    fixed value 4 all instances of this entity
    https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate#Using_a_Generated_Primary_Key

    PRO: contract "hashCode has to be constant while in a hashSet" fulfulled
    CON: all instances will be assigned the same Hash-Bucket - negative performance
    OK, BECAUSE: Hibernate is bad with large collections, so the effect ist mitigated
     */
    return getClass().getSimpleName().hashCode();
  }
}
