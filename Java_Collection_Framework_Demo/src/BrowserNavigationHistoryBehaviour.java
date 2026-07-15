import java.util.Stack;

public class BrowserNavigationHistoryBehaviour {

    private Stack<String> backstack = new Stack<>();
    private Stack<String> forwardStack = new Stack<>();
    private String currentPage;


    public BrowserNavigationHistoryBehaviour(String initialPage) {
        currentPage = initialPage;
    }

    public void visit(String url) {
        backstack.push(currentPage);
        currentPage = url;
        forwardStack.clear();
    }

    public void back(){
        if(backstack.isEmpty()){
            System.out.println("Cannot go back, no previous page.");
            return;
        }
        forwardStack.push(currentPage);
        currentPage = backstack.pop();
    }

    public void forward(){
        if(forwardStack.isEmpty()){
            System.out.println("Cannot go forward, no next page.");
            return;
        }
        backstack.push(currentPage);
        currentPage = forwardStack.pop();
    }

    public void printHistory(){
        System.out.println("Back Stack: " + backstack);
        System.out.println("Current Page: " + currentPage);
        System.out.println("Forward Stack: " + forwardStack);
    }

    public static void main(String[] args) {
        BrowserNavigationHistoryBehaviour browser = new BrowserNavigationHistoryBehaviour("home.com");
        browser.visit("page1.com");
        browser.visit("page2.com");
        browser.visit("page3.com");

        browser.printHistory();

        browser.back();
        browser.printHistory();

        browser.back();
        browser.printHistory();

        browser.forward();
        browser.printHistory();
    }


}
