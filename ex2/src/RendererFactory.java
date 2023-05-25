public class RendererFactory {


    public Renderer buildRenderer(String type) {
        /**
            this methode build a Renderer
            according to its input and return
            a Renderer interface that holds the given renderer
            @Params: String type
            @Returns: Renderer interface that holds the given renderer
         */
        switch (type) {
            case ("console"):
                Renderer renderer = new ConsoleRenderer();
                return renderer;
            case ("none"):
                Renderer renderer1 = new VoidRenderer();
                return renderer1;
            default:
                return null;
        }
    }
}
