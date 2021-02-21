package Content;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ContentInterpreter {
    private ContentType[] contentTypes;
    private static ContentInterpreter instance;

    public ContentType[] InterpretContent(List<String[]> loadedFiles) {
        contentTypes = new ContentType[loadedFiles.size()];

        for(int x = 0; x < loadedFiles.size(); x++) {
            String[] currentFile = loadedFiles.get(x);
            contentTypes[x] = new ContentType();
            for (int y = 0; y < currentFile.length; y++) {
                int i = currentFile[y].indexOf("{");
                if (i == -1) {
                    i = currentFile[y].indexOf("}");
                    if (i == -1) {
                        int index = currentFile[y].indexOf(":");
                        String func = currentFile[y].substring(0, index).trim().toLowerCase();
                        func = func.substring(1, func.length() - 1);
                        String params = currentFile[y].substring(index + 1).trim().toLowerCase();
                        if(params.substring(params.length() - 1) == ",") {
                            params = params.substring(0, params.length() - 1);
                        }
                        params = params.substring(1, params.length() - 1).trim();
                        try {
                            callFunction(func, params, x, loadedFiles.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
    }

    private void callFunction(String func, String params, int i, String[] src) {
        java.lang.reflect.Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(func, String.class, int.class, String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(method != null) {
            try {
                method.invoke(this, params, i, src);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void name(String args, int i, String[] src) {
        contentTypes[i].setName(args);
    }

    private void type(String args, int i, String[] src) {
        Type type = new Type(Type.SubType.valueOf(args));
        contentTypes[i].type = type;
    }
}