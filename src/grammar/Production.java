package grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Production {
    private final String lhs;
    private final List<String> rhs;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = new ArrayList<>(rhs);
    }

    public String getLhs() {
        return lhs;
    }

    public List<String> getRhs() {
        return new ArrayList<>(rhs);
    }

    @Override
    public String toString() {
        return lhs + " -> " + String.join(" ", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Production)) return false;
        Production that = (Production) o;
        return Objects.equals(lhs, that.lhs) && Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}