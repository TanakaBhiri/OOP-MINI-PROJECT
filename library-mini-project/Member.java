import java.util.*;
public class Member {
    private final String id,name; private final List<LibraryItem> loans=new ArrayList<>();
    public Member(String id,String name){if(id==null||id.isBlank()||name==null||name.isBlank())throw new IllegalArgumentException("id and name required");this.id=id;this.name=name;}
    public String getId(){return id;} public String getName(){return name;} List<LibraryItem> getLoans(){return Collections.unmodifiableList(loans);}
    void addLoan(LibraryItem i){loans.add(i);} void removeLoan(LibraryItem i){loans.remove(i);}
    public String toCsv(){return id+","+name;} public String toString(){return id+": "+name+" (active loans="+loans.size()+")";}
}