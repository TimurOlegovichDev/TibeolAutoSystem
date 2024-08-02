package ui.messageSrc.commands;

public interface Commandable {

    default String[] getStringArray(){
        String[] strings = new String[Commands.values().length];
        int index = 0;
        for(Commands command : Commands.values())
            strings[index++] = command.getCommand();
        return strings;
    }
}
