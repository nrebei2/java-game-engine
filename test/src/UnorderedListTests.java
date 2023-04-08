import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import util.ecs.Identifiable;
import util.UnorderedList;

class Stub extends Identifiable {
    int a;

    public Stub(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stub stub = (Stub) o;
        return a == stub.a;
    }
}

public class UnorderedListTests {
    @Test
    void addEmpty() {
        UnorderedList<Stub> list = new UnorderedList<>();
        assertTrue(list.isEmpty());
        list.add(new Stub(10));
        assertFalse(list.isEmpty());
    }

    @Test
    void removeContains() {
        Stub s1 = new Stub(10);
        Stub s2 = new Stub(20);
        Stub s3 = new Stub(30);
        UnorderedList<Stub> list = new UnorderedList<Stub>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        assertTrue(list.contains(s1));
        assertTrue(list.contains(s1));
        assertTrue(list.remove(s1));
        assertFalse(list.remove(new Stub(15)));
        assertFalse(list.contains(s1));
    }

    @Test
    void intoIter() {
        Stub s1 = new Stub(10);
        Stub s2 = new Stub(20);
        Stub s3 = new Stub(30);
        UnorderedList<Stub> list = new UnorderedList<Stub>();
        list.add(s1);
        list.add(s2);
        list.add(s3);

        int count = 0;
        var iter = list.iterator();
        while (iter.hasNext()) {
            count++;
            var elm = iter.next();
            assertTrue(elm.a == 10 || elm.a == 20 || elm.a == 30);
        }
        assertTrue(count == 3);
    }

    @Test
    void iterRemove() {
        Stub s1 = new Stub(10);
        Stub s2 = new Stub(20);
        Stub s3 = new Stub(30);
        UnorderedList<Stub> list = new UnorderedList<Stub>();
        list.add(s1);
        list.add(s2);
        list.add(s3);

        var iter = list.iterator();
        while (iter.hasNext()) {
            var elm = iter.next();
            if (elm.a == 10) iter.remove();
        }
        assertFalse(list.contains(s1));
    }
}
