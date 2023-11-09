# Note: 
    om ni märker att ni får error angående imgages när ni kör koden så kan det bero på att filen har annan packet sortiring 
    i eran kodnings miljö ni kan fixa detta via att göra en av de följande alterbative: 
    1) att fixa klassen ImageIn i packetet tools: ni kan sätta in "./src/"+ innan String:en file i ImageIn:s statiska metod:

```java
    public class ImageIn {
    private static Image img;

        public static Image getImage(String file, int w, int h){
            try{
                img = ImageIO.read(new File("./src/" + file));
                img = img.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            }catch(IOException e){
                            System.out.println(e.getMessage() +": "+ file);
            }
            return img;
        }
    
    }



```

    2) annars kan ni ändra alla ställe som använder denna klass via att skriva "./src/" innan "/com/..." ex:

```java
    Image logo = ImageIn.getImage("./src/com/bitsplease/scribble/img/logo.png", 100, 110);

```
