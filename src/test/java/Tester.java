public class Tester {
    public static void main(String[] args) {
        boolean matched = Main.matchPattern("apple", "[^xyz]");
        System.out.println(matched ? "Found" : "Not Found");
    }
}
