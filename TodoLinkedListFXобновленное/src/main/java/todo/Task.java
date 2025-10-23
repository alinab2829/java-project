package todo;
//модель задачи для демонстрации LinkedTastk
public class Task {
    public String title;
    public boolean done;

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    @Override
    //переопределение метода из класса object
    public String toString (){
        // отображение списков
        return (done ? "✔":"❌") + title;
    }
}
