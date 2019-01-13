public class Main {

    public static void main(String[] args) {
	    Window window = new Window();
	    char[] pass1 = {'z','a','q','1','@','W','S','X'};
	    char[] pass2 = {'a','s','d','f','1','2','3','4'};
	    System.out.println(window.hashPass(pass1));
        System.out.println(window.hashPass(pass2));
    }
}
