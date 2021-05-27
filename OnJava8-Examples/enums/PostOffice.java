// enums/PostOffice.java
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.
// Modeling a post office
package enums;
import java.util.*;
import onjava.*;

class Mail {
  // The NO's reduce probability of random selection:
  enum GeneralDelivery {YES,NO1,NO2,NO3,NO4,NO5}
  enum Scannability {UNSCANNABLE,YES1,YES2,YES3,YES4}
  enum Readability {ILLEGIBLE,YES1,YES2,YES3,YES4}
  enum Address {INCORRECT,OK1,OK2,OK3,OK4,OK5,OK6}
  enum ReturnAddress {MISSING,OK1,OK2,OK3,OK4,OK5}
  GeneralDelivery generalDelivery;
  Scannability scannability;
  Readability readability;
  Address address;
  ReturnAddress returnAddress;
  static long counter = 0;
  long id = counter++;
  @Override public String toString() {
    return "Mail " + id;
  }
  public String details() {
    return toString() +
      ", General Delivery: " + generalDelivery +
      ", Address Scannability: " + scannability +
      ", Address Readability: " + readability +
      ", Address Address: " + address +
      ", Return address: " + returnAddress;
  }
  // Generate test Mail:
  public static Mail randomMail() {
    Mail m = new Mail();
    m.generalDelivery =
      Enums.random(GeneralDelivery.class);
    m.scannability =
      Enums.random(Scannability.class);
    m.readability =
      Enums.random(Readability.class);
    m.address = Enums.random(Address.class);
    m.returnAddress =
      Enums.random(ReturnAddress.class);
    return m;
  }
  public static
  Iterable<Mail> generator(final int count) {
    return new Iterable<Mail>() {
      int n = count;
      @Override public Iterator<Mail> iterator() {
        return new Iterator<Mail>() {
          @Override public boolean hasNext() {
            return n-- > 0;
          }
          @Override public Mail next() {
            return randomMail();
          }
          @Override
          public void remove() { // Not implemented
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }
}

public class PostOffice {
  enum MailHandler {
    GENERAL_DELIVERY {
      @Override boolean handle(Mail m) {
        switch(m.generalDelivery) {
          case YES:
            System.out.println(
              "Using general delivery for " + m);
            return true;
          default: return false;
        }
      }
    },
    MACHINE_SCAN {
      @Override boolean handle(Mail m) {
        switch(m.scannability) {
          case UNSCANNABLE: return false;
          default:
            switch(m.address) {
              case INCORRECT: return false;
              default:
                System.out.println(
                  "Delivering "+ m + " automatically");
                return true;
            }
        }
      }
    },
    VISUAL_INSPECTION {
      @Override boolean handle(Mail m) {
        switch(m.readability) {
          case ILLEGIBLE: return false;
          default:
            switch(m.address) {
              case INCORRECT: return false;
              default:
                System.out.println(
                  "Delivering " + m + " normally");
                return true;
            }
        }
      }
    },
    RETURN_TO_SENDER {
      @Override boolean handle(Mail m) {
        switch(m.returnAddress) {
          case MISSING: return false;
          default:
            System.out.println(
              "Returning " + m + " to sender");
            return true;
        }
      }
    };
    abstract boolean handle(Mail m);
  }
  static void handle(Mail m) {
    for(MailHandler handler : MailHandler.values())
      if(handler.handle(m))
        return;
    System.out.println(m + " is a dead letter");
  }
  public static void main(String[] args) {
    for(Mail mail : Mail.generator(10)) {
      System.out.println(mail.details());
      handle(mail);
      System.out.println("*****");
    }
  }
}
/* Output:
Mail 0, General Delivery: NO2, Address Scannability:
UNSCANNABLE, Address Readability: YES3, Address
Address: OK1, Return address: OK1
Delivering Mail 0 normally
*****
Mail 1, General Delivery: NO5, Address Scannability:
YES3, Address Readability: ILLEGIBLE, Address Address:
OK5, Return address: OK1
Delivering Mail 1 automatically
*****
Mail 2, General Delivery: YES, Address Scannability:
YES3, Address Readability: YES1, Address Address: OK1,
Return address: OK5
Using general delivery for Mail 2
*****
Mail 3, General Delivery: NO4, Address Scannability:
YES3, Address Readability: YES1, Address Address:
INCORRECT, Return address: OK4
Returning Mail 3 to sender
*****
Mail 4, General Delivery: NO4, Address Scannability:
UNSCANNABLE, Address Readability: YES1, Address
Address: INCORRECT, Return address: OK2
Returning Mail 4 to sender
*****
Mail 5, General Delivery: NO3, Address Scannability:
YES1, Address Readability: ILLEGIBLE, Address Address:
OK4, Return address: OK2
Delivering Mail 5 automatically
*****
Mail 6, General Delivery: YES, Address Scannability:
YES4, Address Readability: ILLEGIBLE, Address Address:
OK4, Return address: OK4
Using general delivery for Mail 6
*****
Mail 7, General Delivery: YES, Address Scannability:
YES3, Address Readability: YES4, Address Address: OK2,
Return address: MISSING
Using general delivery for Mail 7
*****
Mail 8, General Delivery: NO3, Address Scannability:
YES1, Address Readability: YES3, Address Address:
INCORRECT, Return address: MISSING
Mail 8 is a dead letter
*****
Mail 9, General Delivery: NO1, Address Scannability:
UNSCANNABLE, Address Readability: YES2, Address
Address: OK1, Return address: OK4
Delivering Mail 9 normally
*****
*/
