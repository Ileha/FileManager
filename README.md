# FileManager
Простой файловый менеджер написанный на Java с возможностью подключения новых модулей.  
Несколько методов, объединенных общем описанием обрабатываемых ими структур данных, составляют класс. Особенность модульного решения на классах состоит в том, что отдельные модули могут разрабатываться, транслироваться и тестироваться независимо друг от друга, для чего им могут потребоваться описания интерфейсов взаимодействия.
Для реализации модульности в основной программе необходимо создать абстрактный класс(интерфейс) и добавить туда абстрактный метод ```javaABSExecCommand(ArrayList<String> arguments)```, который будет переопределяться в модулях, унаследованных от этого абстрактного класса. Кроме того, для основной программы важно название каждого модуля в отдельности, а для модуля необходимо иметь доступ к данным основной программы. Поэтому в абстрактный класс необходимо добавить поле "name" и ссылку на класс с данными Data (для примера в нём будет храниться путь текущей директории).  
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
В абстрактном классе могут быть реализованы дополнительные методы для модулей. В качестве аргументов можно передавать ключи, которые изменят вывод данных о содержимом каталога (например, будут печатать дату изменения каждого). Для этого понадобится какая-либо функция, возвращающая список этих ключей. Так как эта функция, возможно, понадобиться не только в модуле ls, но и в каких-либо иных, поэтому лучше один раз создать её в абстрактном классе, а потом использовать в любом модуле. Кроме того, можно добавить проверку на минимальное количество аргументов, а в качестве описания модуля добавить в абстрактном классе абстрактный метод, который при вызове у потомка будет печатать help по модулю-команде.
Основная программа представляет собой разделитель строк на слова и поисковик внутри списка заранее добавленных туда экземпляров классов модулей. По первому слову (имени команды) осуществляется её поиск и вызов метода абстрактного класса ABSExecCommand с передачей туда всех аргументов в виде списка строковых переменных. Дальше идёт выполнение собственно кода модуля. Для основной программы не важна внутренняя организация модуля и поэтому можно легко поменять или добавить модули в программу.

Для того чтобы основная программа сама находила скомпилированные отдельно от неё классы с командами, загружала их в себя и создавала их объекты, необходимо создать собственный загрузчик модулей.
При реализации загрузчика модулей стоит помнить следующее:
- любой загрузчик должен явно или неявно расширять класс ```javajava.lang.ClassLoader.```
- любой загрузчик должен поддерживать модель делегирования загрузки, образуя иерархию.
- в классе ```javajava.lang.ClassLoader``` уже реализован метод непосредственной загрузки — ```javadefineClass(...)```, который байт-код преобразует в ```javajava.lang.Class```, осуществляя его валидацию.
- механизм рекурентного поиска также реализован в классе ```javajava.lang.ClassLoader``` и заботиться об это не нужно.
- для корректной реализации загрузчика достаточно лишь переопределить метод ```javafindClass()``` класса ```javajava.lang.ClassLoader```.
Сдедовательно, для правильной реализации загрузчиков рекомендуется придерживаться указанного сценария — переопределения метода ```javafindClass()```.
Для реализации этого создадим собственный загрузчик модулей ```javaModuleLoader```, наследуемый от класса ```javaClassLoader```. 
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
В основной программе необходимо вызвать метод ```javaloadClass``` нашего класса, в который передаются названия модулей, находящихся в директории ```javapathtobin```. Далее необходимо экземпляр закруженного класса и добавить его в список доступных команд:
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
Таким образом, для добавления нового функционала в программу достаточно скопировать скомпилированный файл в определённый каталог.
