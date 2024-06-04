package com.sibertech.lib.conf;

public class Conf {

    private static Conf instance = null;

    private static  String mcServName = "No Name";
    private DSet    data = null;
    private ConfApp app  = null;

    private Conf () {
        data = new DSet();
        app  = new ConfApp();
    }

    // должен быть вызван первым, раньше, чем вызван inst() без параметра.
    // По крайней мере, до того как будут созданы объекты LogWriter.
    public static Conf inst(String mcServName) {
        Conf.mcServName = mcServName;
        return inst();
    }

    public static Conf inst() {
        if (instance == null)
        {
            synchronized(Conf.class) {
                if (Conf.instance == null) {
                    Conf.instance = new Conf ();

                    Conf.instance.data.init();
                }
            }
        }
        return instance;
    }

    public ConfApp app  () {return app;}
    public DSet    data () {return data;}

    public static String  mcServName () {return mcServName;}
}
