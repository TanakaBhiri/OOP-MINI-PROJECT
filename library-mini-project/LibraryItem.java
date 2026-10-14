import java.time.LocalDate;
public abstract class LibraryItem implements Borrowable {
    private final String id, title; private boolean available=true; private Member currentMember; private LocalDate borrowedOn;
    protected LibraryItem(String id,String title){if(id==null||id.isBlank()||title==null||title.isBlank())throw new IllegalArgumentException("id and title required");this.id=id;this.title=title;}
    public abstract int loanPeriodDays(); public String getId(){return id;} public String getTitle(){return title;} public boolean isAvailable(){return available;}
    public LocalDate getDueDate(){return borrowedOn==null?null:borrowedOn.plusDays(loanPeriodDays());}
    public Member getCurrentMember(){return currentMember;}
    public void borrow(Member member)throws ItemNotAvailableException,MemberLimitExceededException{if(!available)throw new ItemNotAvailableException("Item already on loan: "+title);if(member.getLoans().size()>=3)throw new MemberLimitExceededException("Member may hold at most 3 loans");available=false;currentMember=member;borrowedOn=LocalDate.now();member.addLoan(this);}
    public void giveBack(){if(available)throw new IllegalStateException("Item is not borrowed");currentMember.removeLoan(this);currentMember=null;borrowedOn=null;available=true;}
    public String toCsv(){return getClass().getSimpleName()+","+id+","+title+","+available+","+(currentMember==null?"":currentMember.getId())+","+(borrowedOn==null?"":borrowedOn);}
    public String toString(){return getClass().getSimpleName()+"["+id+"] "+title+" ("+(available?"available":"on loan, due "+getDueDate())+")";}
}