package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAILY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POM;

import java.util.stream.Stream;
import seedu.address.logic.commands.SetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.settings.PetName;

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

        PetName petName =
                (argMultimap.getValue(PREFIX_PET).isEmpty())
                        ? ParserUtil.parsePetName("")
                        : ParserUtil.parsePetName(argMultimap.getValue(PREFIX_PET).get());

        // PomDuration pomDuration =
        //         (argMultimap.getValue(PREFIX_POM).isEmpty())
        //                 ? ParserUtil.parsePomDuration("")
        //                 : ParserUtil.parsePomDuration(argMultimap.getValue(PREFIX_POM).get());

        // DailyTarget dailyTarget =
        //         (argMultimap.getValue(PREFIX_DAILY).isEmpty())
        //                 ? ParserUtil.parseDailyTarget("")
        //                 : ParserUtil.parseDailyTarget(argMultimap.getValue(PREFIX_DAILY).get());

        // return new SetCommand(petName, pomDuration, dailyTarget);
        return new SetCommand(petName);
    }

    private static boolean arePrefixesPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
