package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAILY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POM;

import java.util.stream.Stream;
import seedu.address.logic.commands.SetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.settings.DailyTarget;
import seedu.address.model.settings.PetName;
import seedu.address.model.settings.PomDuration;

public class SetCommandParser implements Parser<SetCommand> {

    public SetCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PET, PREFIX_POM, PREFIX_DAILY);

        if (!(arePrefixesPresent(argMultimap, PREFIX_PET)
                        || arePrefixesPresent(argMultimap, PREFIX_POM)
                        || arePrefixesPresent(argMultimap, PREFIX_DAILY))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE));
        }

        PetName petName;
        PomDuration pomDuration;
        DailyTarget dailyTarget;

        if (arePrefixesPresent(argMultimap, PREFIX_PET)) {
            petName =
                    (argMultimap.getValue(PREFIX_PET).isEmpty())
                            ? ParserUtil.parsePetName("")
                            : ParserUtil.parsePetName(argMultimap.getValue(PREFIX_PET).get());
        } else {
            petName = new PetName("");
        }

        if (arePrefixesPresent(argMultimap, PREFIX_POM)) {
            pomDuration =
                    (argMultimap.getValue(PREFIX_POM).isEmpty())
                            ? ParserUtil.parsePomDuration("")
                            : ParserUtil.parsePomDuration(argMultimap.getValue(PREFIX_POM).get());
        } else {
            pomDuration = new PomDuration("");
        }

        if (arePrefixesPresent(argMultimap, PREFIX_DAILY)) {
            dailyTarget =
                    (argMultimap.getValue(PREFIX_DAILY).isEmpty())
                            ? ParserUtil.parseDailyTarget("")
                            : ParserUtil.parseDailyTarget(argMultimap.getValue(PREFIX_DAILY).get());
        } else {
            dailyTarget = new DailyTarget("");
        }

        return new SetCommand(petName, pomDuration, dailyTarget);
    }

    private static boolean arePrefixesPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
