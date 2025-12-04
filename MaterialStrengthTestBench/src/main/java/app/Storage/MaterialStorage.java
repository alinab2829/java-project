package app.Storage;

import app.Material.Material;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

// Класс MaterialStorage — утилита для сохранения и загрузки списка материалов в JSON-файл
public final class MaterialStorage {


    // Gson с красивым форматированием для JSON
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Тип списка Material для корректной десериализации
    private static final Type LIST_TYPE = new TypeToken<List<Material>>(){}.getType();

    // Путь к файлу materials.json в домашней директории пользователя
    private static final Path FILE = Paths.get(System.getProperty("user.home"), "materials.json");

    // Загрузка списка материалов из файла
    public static List<Material> load() {
        try {
            if (Files.exists(FILE)) { // проверяем, существует ли файл
                String json = Files.readString(FILE, StandardCharsets.UTF_8); // читаем весь файл
                List<Material> list = GSON.fromJson(json, LIST_TYPE);        // десериализуем JSON в список
                if (list != null) return list;
            }
        } catch (Exception ignored) {} // игнорируем ошибки чтения/парсинга
        return new ArrayList<>(); // если файла нет или произошла ошибка — возвращаем пустой список
    }

    // Сохранение списка материалов в файл
    public static void save(List<Material> list) {
        try {
            String json = GSON.toJson(list, LIST_TYPE); // сериализуем список в JSON
            Files.writeString(FILE, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,             // создаём файл, если его нет
                    StandardOpenOption.TRUNCATE_EXISTING); // перезаписываем существующий
        } catch (IOException e) {
            System.err.println("Не удалось сохранить материалы: " + e.getMessage());
        }
    }

    // Добавление нового материала к существующему списку
    public static void add(Material m) {
        List<Material> list = load(); // загружаем текущий список
        list.add(m);                  // добавляем новый материал
        save(list);                   // сохраняем обновлённый список
    }

    // Очистка всех материалов (удаление содержимого файла)
    public static void clear() {
        save(new ArrayList<>()); // сохраняем пустой список
    }

    // Получение пути к файлу
    public static Path filePath() {
        return FILE;
    }


}
