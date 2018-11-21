package projekti.UI;

import java.util.ArrayList;
import java.util.List;

public class StubIO implements IO {

    private List<String> lines;
    private int i;
    private ArrayList<String> prints;

    public StubIO(List<String> lines) {
        this.lines = lines;
        prints = new ArrayList<>();
    }

    @Override
    public void print(String s) {
        prints.add(s);
    }

    @Override
    public String getInput() {
        if (i < lines.size()) {
            return lines.get(i++);
        }
        return "";
    }

    public ArrayList<String> getPrints() {
        return prints;
    }
}
