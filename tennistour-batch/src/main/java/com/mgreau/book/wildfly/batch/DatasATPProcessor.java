package com.mgreau.book.wildfly.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

import com.mgreau.book.wildfly.entities.Match;
import com.mgreau.book.wildfly.entities.Player;
import com.mgreau.book.wildfly.entities.PlayerBean;
import com.mgreau.book.wildfly.entities.Tournament;

@Named
public class DatasATPProcessor implements ItemProcessor {
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

	@Inject
	PlayerBean pBean;

	@Override
	public Match processItem(Object s) {
		if (s == null || ((String)s).trim().equals(""))
			return null;
		
		Scanner scan = new Scanner(s.toString());
		scan.useDelimiter(",");

		Match m = new Match();
		Tournament tournament = new Tournament();
		Player player1 = new Player();
		Player player2 = new Player();
		
		String assoc = scan.next();
		tournament.setTypeAssociation(assoc);

		tournament.setIdAssociation(scan.nextInt());
		tournament.setLocation(scan.next());
		tournament.setName(scan.next());
		

		Date matchDate = new Date();
		try {
			matchDate = format.parse(scan.next());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(matchDate);

		tournament.setYear(c.get(Calendar.YEAR));
		m.setDate(matchDate);
		scan.next(); // Series : data not used
		tournament.setCourt(scan.next());
		tournament.setSurface(scan.next());

		m.setRound(scan.next());
		tournament.setBestOfSet(scan.nextInt());

		player1.setName(scan.next());
		player1.setSexe(assoc.equals("ATP")?'M':'F');

		player2.setName(scan.next());
		player2.setSexe(assoc.equals("ATP")?'M':'F');

		m.setPlayer1(player1);
		m.setPlayer2(player2);
		m.setTournament(tournament);
		scan.close();
		return m;

	}
}
