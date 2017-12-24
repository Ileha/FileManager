# FileManager
Простой файловый менеджер написанный на Java с возможностью подключения новых модулей.  
Несколько методов, объединенных общем описанием обрабатываемых ими структур данных, составляют класс. Особенность модульного решения на классах состоит в том, что отдельные модули могут разрабатываться, транслироваться и частично отлаживаться отдельно друг от друга. И для этого им могут потребоваться описания интерфейсов взаимодействия.
Для реализации модульности в основной программе создадим абстрактный класс(интерфейс) и добавим туда абстрактный метод ```javaABSExecCommand(ArrayList<String> arguments)```, который будут переопределяется в модулях, унаследованных от этого абстрактного класса. Также для основной программы важно название каждого модуля в отдельности, а для модуля важно иметь доступ к данным основной программы. Поэтому добавим в абстрактный класс, поле name и ссылку на класс с данными Data (для примера в нём будет хранится путь текущей директории).  
```java
public abstract class ABSCommand {
    private String name;
    protected Data WorkDir;

    public ABSCommand(String _name, Data _dir) {
        name = _name;
        WorkDir = _dir;
    }
    public abstract void ABSExecCommand(ArrayList<String> commands) throws FileNotFound, IOException, ExeptionOnCommand;
}
```
Именно этот класс станет интерфейсом взаимодействия основной программы с модулем. В качестве примера модуля возьмём команду вывода содержимого каталога ls.
```java
public class Command_ls extends ABSCommand {
    public Command_ls(Data _data) {
        super("ls",_data); //конструктор класса ABSCommand 
    }

    @Override
    public void ABSExecCommand(ArrayList<String> arguments) {
        File f = null;
        if (Commands.size() == 1) {
            f = WorkDir.directory.current_directory;
        }
        else {
            try {
                f = WorkDir.directory.GetFileOrDirectory(Commands.get(1), TypeOfFile.Directory);
            }
            catch (FileNotFound err) {
                throw err;
            }
        }
        boolean key_l = FindKey("-l");
        for (File item : f.listFiles()) {
            if (item.isDirectory()) {
                System.out.println(item.getName() + "\tкаталог"+"\t"+for_key_l(key_l,item));
            } else {
                System.out.println(item.getName() + "\tфайл"+"\t"+for_key_l(key_l,item));
            }
        }
    }
}
```
В абстрактном классе могут быть реализованы дополнительные методы для модулей. В качестве аргументов можно передавать ключи которые изменят вывод данных о содержимом каталога (например будет печатать дату изменения каждого). Для этого понадобится какая либо функция, которая возвращает список этих ключей. Так как она возможно понадобиться не только в модуле ls, но и в каких либо других, поэтому лучше один раз создать её в абстрактном классе, а потом использовать в каком угодно модуле. Так же можно добавить проверку на минимальное количество аргументов. В качестве описания модуля можно добавить в абстрактном классе абстрактный метод, который при вызове у потомка печатал help по модулю-команде.
Основная программа представляет собой разделитель строк в слова и поисковик внутри списка заранее туда добавленных экземпляров классов модулей. По первому слову (имени команды) осуществляется её поиск и вызов метода абстрактного класса ABSExecCommand с передачей туда всех аргументов в виде списка строковых переменных. Дальше идёт выполнение собственно кода модуля. Для основной программы не важна внутренняя организация модуля и поэтому можно легко поменять или добавить модули в программу.

Для того чтобы основная программа сама находила скомпилированные отдельно от неё классы с командами и загружала их в себя, и создавала их объекты нужно создать собственный загрузчик модулей.
При его реализации нужно помнить следующее:
- любой загрузчик должен явно или неявно расширять класс ```javajava.lang.ClassLoader.```
- любой загрузчик должен поддерживать модель делегирования загрузки, образуя иерархию.
- в классе ```javajava.lang.ClassLoader``` уже реализован метод непосредственной загрузки — ```javadefineClass(...)```, который байт-код преобразует в ```javajava.lang.Class```, осуществляя его валидацию.
- механизм рекурентного поиска также реализован в классе ```javajava.lang.ClassLoader``` и заботиться об это не нужно.
- для корректной реализации загрузчика достаточно лишь переопределить метод ```javafindClass()``` класса ```javajava.lang.ClassLoader```.
Поэтому для правильной реализации загрузчиков рекомендуется придерживаться указанного сценария — переопределения метода ```javafindClass()```.
Для реализации этого создадим собственный загрузчик модулей ```javaModuleLoader``` наследуемый от класса ```javaClassLoader```. 
```java
public class ModuleLoader extends ClassLoader {
    private String pathtobin;
    public String GetWorkDir() { return pathtobin; }

    public ModuleLoader(String pathtobin, ClassLoader parent) {
        super(parent);
        this.pathtobin = pathtobin;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException { //переопределение метода findClass()
        try {
            byte b[] = fetchClassFromFS(pathtobin+ "/" + className + ".class"); //метод класса вернёт файл в виде массив байт
            return defineClass(className, b, 0, b.length); //Преобразует массив байт в экземпляр класса Class
        } catch (FileNotFoundException ex) {
            return super.findClass(className);
        } catch (IOException ex) {
            return super.findClass(className);
        }
    }
}
```
В основной программе вызываем метод ```javaloadClass``` нашего класса в который передаём названия модулей которые находятся в директории ```javapathtobin```. Далее создаём экземпляр закруженного класса и добавляем его в список доступных команд:
```java
modulePath = "рабочая директория";

loader = new ModuleLoader(modulePath, ClassLoader.getSystemClassLoader());//наш загрузчик

File dir = new File(loader.GetWorkDir());

for (String module : dir.list()) {//получение всех файлов из рабочей директории
    String moduleName = module.split(".class")[0];
    Class clazz = loader.loadClass(moduleName);//Загружает класс по имени
    ABSCommand _module = (ABSCommand)clazz.getConstructor(Data.class).newInstance(data);//создание экземпляра модуля(команды)
    data.exec_commands.add(_module); //добавление в список доступных команд
}
```
Таким образом чтобы добавить новый функционал в программу достаточно скопировать скомпилированный файл в определённый каталог.
