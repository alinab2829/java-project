package todo;

import java.util.LinkedList;
import java.util.List;

public class LinkedTasks {
    private final LinkedList<Task> list = new LinkedList<>();
    public int size(){return list.size(); }//возвращает сколько задач в списке
    public List<Task> asList() {return list;}//дает достпут ко всему списку
    public void addLast(String title) {list.add(new Task(title));}

    public void toogleAt(int index){//задача выполнена или нет
        if(!valid(index)) return;
        list.get(index).done = !list.get(index).done;
    }

    public void removeAt(int index){//удаление задачи
        if(!valid(index)) return;
        list.remove(index);
    }

//    переместить элемент на +1 вниз и на -1 вверх

    public  void move(int index, int delta){//преставление задачи вниз вврех
        int target = index + delta;
        if(!valid(index) || !valid(target)) return;
        Task t = list.remove(index);
        list.add(target, t);

    }

    public void clean(){list.clear();}//удаление всех задач

    private boolean valid(int i){return i >= 0 && i< list.size();}
}
