package subject;

public enum Subject {

    POS("Programmieren", "SCRE"),
    AM("Mathe", "RIEE"),;

    private String name;
    private String teacher;

    private Subject(String name, String teacher){
        this.name = name;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
