import java.io.IOException;import java.nio.file.*;import java.time.LocalDate;import java.util.*;import java.util.stream.*;
public class Library {
    private final Map<String,LibraryItem> items=new LinkedHashMap<>();private final Map<String,Member> members=new LinkedHashMap<>();
    private final Path itemsFile=Path.of("library-items.csv"),membersFile=Path.of("library-members.csv");
    public void addItem(LibraryItem i){if(items.putIfAbsent(i.getId(),i)!=null)throw new IllegalArgumentException("Duplicate item id");}
    public void registerMember(Member m){if(members.putIfAbsent(m.getId(),m)!=null)throw new IllegalArgumentException("Duplicate member id");}
    public Collection<LibraryItem> getItems(){return Collections.unmodifiableCollection(items.values());}public Collection<Member> getMembers(){return Collections.unmodifiableCollection(members.values());}
    public void borrow(String mid,String iid)throws ItemNotAvailableException,MemberLimitExceededException{Member m=members.get(mid);LibraryItem i=items.get(iid);if(m==null)throw new IllegalArgumentException("Unknown member: "+mid);if(i==null)throw new IllegalArgumentException("Unknown item: "+iid);i.borrow(m);}
    public void giveBack(String iid){LibraryItem i=items.get(iid);if(i==null)throw new IllegalArgumentException("Unknown item: "+iid);i.giveBack();}
    public List<LibraryItem> searchByTitle(String q){String s=q.toLowerCase(Locale.ROOT);return items.values().stream().filter(i->i.getTitle().toLowerCase(Locale.ROOT).contains(s)).sorted(Comparator.comparing(LibraryItem::getTitle)).toList();}
    public List<LibraryItem> overdueItems(){LocalDate today=LocalDate.now();return items.values().stream().filter(i->!i.isAvailable()&&i.getDueDate().isBefore(today)).toList();}
    public void save()throws IOException{Files.write(itemsFile,items.values().stream().map(LibraryItem::toCsv).toList());Files.write(membersFile,members.values().stream().map(Member::toCsv).toList());}
    public void load()throws IOException{
        if(Files.exists(membersFile))for(String line:Files.readAllLines(membersFile)){if(line.isBlank())continue;String[] p=line.split(",",2);if(p.length==2)members.putIfAbsent(p[0],new Member(p[0],p[1]));}
        if(Files.exists(itemsFile)){List<String[]> rows=Files.readAllLines(itemsFile).stream().filter(s->!s.isBlank()).map(s->s.split(",",-1)).toList();
            for(String[] p:rows)if(p.length>=4){LibraryItem i=p[0].equals("Dvd")?new Dvd(p[1],p[2]):new Book(p[1],p[2]);items.putIfAbsent(i.getId(),i);}
            for(String[] p:rows)if(p.length>=6&&p[3].equals("false")&&!p[4].isBlank()){LibraryItem i=items.get(p[1]);Member m=members.get(p[4]);if(i!=null&&m!=null&&i.isAvailable())try{i.borrow(m);}catch(Exception e){System.err.println("Could not restore loan "+i.getId());}}
        }
    }
    public static void main(String[] args){Library l=new Library();try{l.load();}catch(IOException e){System.out.println("Could not load data: "+e.getMessage());}Scanner s=new Scanner(System.in);boolean run=true;
        while(run){System.out.println("\\nLIBRARY: 1 Add item 2 Register member 3 Borrow 4 Return 5 Overdue 6 Search 7 List 0 Save/exit");System.out.print("> ");String c=s.nextLine().trim();try{switch(c){
            case "1"->{System.out.print("Type book/dvd: ");String t=s.nextLine();System.out.print("ID: ");String id=s.nextLine();System.out.print("Title: ");String title=s.nextLine();l.addItem(t.equalsIgnoreCase("dvd")?new Dvd(id,title):new Book(id,title));}
            case "2"->{System.out.print("Member ID: ");String id=s.nextLine();System.out.print("Name: ");l.registerMember(new Member(id,s.nextLine()));}
            case "3"->{System.out.print("Member ID: ");String mid=s.nextLine();System.out.print("Item ID: ");l.borrow(mid,s.nextLine());}
            case "4"->{System.out.print("Item ID: ");l.giveBack(s.nextLine());}
            case "5"->l.overdueItems().forEach(System.out::println);
            case "6"->{System.out.print("Title contains: ");l.searchByTitle(s.nextLine()).forEach(System.out::println);}
            case "7"->{l.getItems().forEach(System.out::println);l.getMembers().forEach(System.out::println);}
            case "0"->{l.save();System.out.println("Saved.");run=false;}
            default->System.out.println("Unknown option.");
        }}catch(Exception e){System.out.println("Operation failed: "+e.getMessage());}}
    }
}