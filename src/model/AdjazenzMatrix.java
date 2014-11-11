package model;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdjazenzMatrix
{
	public static final int	min		= 3;
	public static final int	max		= 50;
	public static final int	start	= 10;
	private boolean[][]		matrix;
	private int				knotenanzahl;

	public AdjazenzMatrix(int n)
	{
		setMatrix(n);
	}

	public int getKnotenanzahl()
	{
		return knotenanzahl;
	}

	private void setMatrix(int knotenanzahl)
	{
		if (knotenanzahl >= min && knotenanzahl <= max)
			{
				this.knotenanzahl = knotenanzahl;
				matrix = new boolean[knotenanzahl][knotenanzahl];
			}
		else
			throw new IllegalArgumentException("Bitte Knotenanzahl zwischen " + AdjazenzMatrix.min + " und " + AdjazenzMatrix.max + " eingeben!");
	}

	private void kantenHinzufuegen(ArrayList<Integer> kantenMemory, int knoten)
	{
		for (int i = 0; i < kantenMemory.size(); i++)
			{
				int kanteNach = kantenMemory.get(i);
				hinzufuegenKante(kanteNach, knoten);
			}
	}

	private ArrayList<Integer> kantenEntfernen(int knoten)
	{
		ArrayList<Integer> kantenMemory = new ArrayList<Integer>();
		boolean value;

		for (int i = 1; i <= knotenanzahl; i++)
			{
				value = getWert(i, knoten);
				if (!value)
					continue;
				kantenMemory.add(i);
				loeschenKante(knoten, i);
			}

		return kantenMemory;
	}

	private ArrayList<Integer> kantenFinden(int knoten)
	{
		ArrayList<Integer> kantenMemory = new ArrayList<Integer>();
		boolean value;

		for (int i = knoten; i < knotenanzahl; i++)
			{
				value = matrix[i][knoten];
				if (!value)
					continue;
				kantenMemory.add(i);
			}

		return kantenMemory;
	}

	private boolean[][] manipulieren(int multiplicator)
	{
		if (multiplicator < 1)
			throw new IllegalArgumentException("Falsche Angabe fuer multiplicator");
		boolean[][] ergebniss = new boolean[knotenanzahl][knotenanzahl];
		boolean[][] temp = new boolean[knotenanzahl][knotenanzahl];
		for (int z = 0; z < knotenanzahl; z++)
			{
				for (int s = z + 1; s < knotenanzahl; s++)
					{
						temp[z][s] = temp[s][z] = matrix[z][s];
					}
			}

		if (multiplicator == 1)
			return temp;

		for (int l = 1; l < multiplicator; l++)
			{
				int i, j, k;
				for (i = 0; i < knotenanzahl; i++)
					for (j = i + 1; j < knotenanzahl; j++)
						{
							for (k = 0; k < knotenanzahl; k++)
								{
									if (temp[i][k] && matrix[k][j])
										{
											ergebniss[i][j] = ergebniss[j][i] = true;
											break;
										}
								}
						}

				for (int z = 0; z < knotenanzahl; z++)
					{
						for (int s = z + 1; s < knotenanzahl; s++)
							{
								temp[z][s] = temp[s][z] = ergebniss[z][s];
							}
					}
			}
		return ergebniss;
	}

	public void hinzufuegenKante(int spalte, int zeile)
	{
		wertPruefung(spalte, zeile);
		if (getWert(spalte, zeile))
			throw new IllegalArgumentException("Kante existiert schon!");
		matrix[spalte - 1][zeile - 1] = true;
		matrix[zeile - 1][spalte - 1] = true;
	}

	public void loeschenKante(int spalte, int zeile)
	{
		wertPruefung(spalte, zeile);
		if (!getWert(spalte, zeile))
			throw new IllegalArgumentException("Kante existiert nicht!");
		matrix[spalte - 1][zeile - 1] = false;
		matrix[zeile - 1][spalte - 1] = false;
	}

	public void changeValue(int zeile, int spalte)
	{
		wertPruefung(zeile, spalte);
		if (getWert(zeile, spalte))
			{
				loeschenKante(zeile, spalte);
			}
		else
			{
				hinzufuegenKante(zeile, spalte);
			}
	}

	public boolean getWert(int zeile, int spalte)
	{
		wertPruefung(zeile, spalte);
		return matrix[zeile - 1][spalte - 1];
	}

	private void wertPruefung(int zeile, int spalte)
	{
		if (zeile < 1 || spalte < 1 || zeile > knotenanzahl || spalte > knotenanzahl)
			{
				System.err.println("Zeile = " + zeile + "\nSpalte = " + spalte + "\nKnotenanzahl = " + knotenanzahl);
				throw new IllegalArgumentException("Falsche Angabe fuer zeile/spalte!");
			}
	}

	private String matrixDrueck(String name, boolean[][] matrix)
	{
		if (matrix != null && name != null)
			{
				StringBuilder sb = new StringBuilder(1000);
				sb.append(name + "\n");

				for (int z = 0; z < matrix.length; z++)
					{
						for (int s = 0; s < matrix.length; s++)
							{
								if (matrix[z][s])
									sb.append("   " + "1");
								else
									sb.append("   " + "0");
							}
						sb.append("\n");
					}
				sb.append(" ");
				for (int i = 0; i < matrix.length; i++)
					{
						sb.append("----");
					}
				sb.append("\n");
				return sb.toString();
			}
		return "";
	}

	private boolean[][] berechneWegmatrix()
	{
		boolean[][] result = new boolean[knotenanzahl][knotenanzahl];
		for (int i = 0; i < knotenanzahl; i++)
			{
				boolean[][] temp = manipulieren(i + 1);
				boolean aenderung = false;
				for (int z = 0; z < knotenanzahl; z++)
					{
						for (int s = z + 1; s < knotenanzahl; s++)
							{
								if (!result[z][s] && temp[z][s])
									{
										result[z][s] = result[s][z] = true;
										aenderung = true;
									}

							}
						result[z][z] = true;
					}
				if (!aenderung)
					break;
			}
		return result;
	}

	private int[][] berechneDistanzmatrix()
	{
		int[][] dmatrix = new int[knotenanzahl][knotenanzahl];
		for (int i = 0; i < knotenanzahl; i++)
			{
				boolean[][] temp = manipulieren(i + 1);
				boolean aenderung = false;
				for (int z = 0; z < knotenanzahl; z++)
					{
						for (int s = z + 1; s < knotenanzahl; s++)
							{
								if (dmatrix[z][s] == 0 && temp[z][s])
									{
										dmatrix[z][s] = dmatrix[s][z] = i + 1;
										aenderung = true;
									}
							}
					}
				if (!aenderung)
					break;
			}
		return dmatrix;
	}

	private int berechneRadius(int[] exz)
	{
		int result = exz[0];
		for (int z = 1; z < exz.length; z++)
			{
				if (exz[z] < result)
					result = exz[z];
			}
		return result;
	}

	private int berechneDurchmesser(int[] exz)
	{
		int result = exz[0];
		for (int z = 1; z < exz.length; z++)
			{
				if (exz[z] > result)
					result = exz[z];
			}
		return result;
	}

	private ArrayList<Integer> berechneZentrum(int[] exz, int radius)
	{
		ArrayList<Integer> zentren = new ArrayList<Integer>();

		for (int z = 0; z < exz.length; z++)
			{
				if (exz[z] == radius)
					zentren.add(z);
			}

		return zentren;
	}

	private int[] berechneExzentrizitaeten(int[][] distanzmatrix)
	{
		int[] result = new int[knotenanzahl];

		for (int z = 0; z < matrix.length; z++)
			{
				int temp = 0;
				for (int s = 0; s < matrix.length; s++)
					{
						if (distanzmatrix[z][s] > temp)
							temp = distanzmatrix[z][s];
					}
				result[z] = temp;
			}
		return result;
	}

	private boolean checkZusammenhaengend(boolean[][] wegmatrix)
	{
		for (int z = 0; z < wegmatrix.length; z++)
			if (!wegmatrix[0][z])
				return false;
		return true;
	}

	private ArrayList<ArrayList<Integer>> berechneKomponenten()
	{
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		boolean[][] wegmatrix = berechneWegmatrix();
		boolean temp[] = new boolean[knotenanzahl];
		int zaehler = knotenanzahl;

		for (int z = 0; z < wegmatrix.length; z++)
			{
				ArrayList<Integer> komponente = new ArrayList<Integer>();
				for (int s = z; s < wegmatrix.length; s++)
					{
						if ((wegmatrix[z][s]) && !temp[s])
							{
								komponente.add(s);
								zaehler--;
								temp[s] = true;
							}
					}
				if (komponente.size() > 0)
					result.add(komponente);
				if (zaehler == 0)
					break;
			}
		return result;
	}

	private ArrayList<ArrayList<ArrayList<Integer>>> berechneKomponentenKanten(ArrayList<ArrayList<Integer>> angabe)
	{
		ArrayList<ArrayList<ArrayList<Integer>>> result3 = new ArrayList<ArrayList<ArrayList<Integer>>>();

		for (int i = 0; i < angabe.size(); i++)
			{
				ArrayList<Integer> liste = angabe.get(i);
				ArrayList<ArrayList<Integer>> result2 = new ArrayList<ArrayList<Integer>>();
				for (int j = 0; j < liste.size(); j++)
					{
						ArrayList<Integer> komponente = kantenFinden(liste.get(j));
						for (int k = 0; k < komponente.size(); k++)
							{

								ArrayList<Integer> result = new ArrayList<Integer>();
								result.add(liste.get(j) + 1);
								result.add(komponente.get(k) + 1);

								if (result.size() > 0)
									result2.add(result);
							}
					}
				if (result2.size() > 1)
					result3.add(result2);
			}
		return result3;
	}

	private boolean pruefeObArtikulation(int knoten, int komponentenanzahl)
	{
		ArrayList<Integer> kantenMemory = kantenEntfernen(knoten);
		boolean check = berechneKomponenten().size() - 1 > komponentenanzahl;
		kantenHinzufuegen(kantenMemory, knoten);
		return check;
	}

	private ArrayList<Integer> getArtikulationen(int komponentenanzahl)
	{
		ArrayList<Integer> artikulationen = new ArrayList<Integer>();
		for (int i = 1; i <= knotenanzahl; i++)
			{
				if (pruefeObArtikulation(i, komponentenanzahl))
					artikulationen.add(i-1);
			}
		return artikulationen;
	}

	private boolean pruefeObBruecke(int zeile, int spalte, int komponentenanzahl)
	{
		boolean check = false;
		if (getWert(zeile, spalte))
			{
				loeschenKante(zeile, spalte);
				check = berechneKomponenten().size() > komponentenanzahl;
				hinzufuegenKante(zeile, spalte);
			}
		return check;
	}

	private boolean[][] getBruecken(int komponentenanzahl)
	{
		boolean[][] bruecken = new boolean[knotenanzahl][knotenanzahl];
		for (int z = 0; z < knotenanzahl; z++)
			for (int s = z + 1; s < knotenanzahl; s++)
				if (pruefeObBruecke(z + 1, s + 1, komponentenanzahl))
					bruecken[z][s] = true;
		return bruecken;
	}

	@SuppressWarnings("unused")
	public String aktualisiereBerechnungen()
	{
		long startTime = System.currentTimeMillis();
		int[][] distanzmatrix = berechneDistanzmatrix();
		boolean[][] wegmatrix = berechneWegmatrix();
		StringBuilder berechnungen = new StringBuilder();
		int[] exzentrizitaeten = berechneExzentrizitaeten(distanzmatrix);
		int radius = berechneRadius(exzentrizitaeten);
		ArrayList<Integer> zentrum = berechneZentrum(exzentrizitaeten, radius);
		int durchmesser = berechneDurchmesser(exzentrizitaeten);
		

		ArrayList<ArrayList<Integer>> komponenten = berechneKomponenten();
		int komponentenanzahl = komponenten.size();
		ArrayList<ArrayList<ArrayList<Integer>>> komponentenKanten = berechneKomponentenKanten(komponenten);
		ArrayList<Integer> artikulationen = getArtikulationen(komponentenanzahl);
		boolean[][] bruecken = getBruecken(komponentenanzahl);

		String info = matrixDrueck("\n   Distanzmatrix", distanzmatrix) + matrixDrueck("   Wegmatrix", wegmatrix);

		berechnungen.append(info);
		if (checkZusammenhaengend(wegmatrix))
			{
				berechnungen.append("   Exzentrizitäten: ");

				for (int i = 0; i < exzentrizitaeten.length; i++)
					{
						berechnungen.append(i + 1).append(": ").append(exzentrizitaeten[i]).append("   ");

					}
				berechnungen.append("\n   Radius: ").append(radius);
				berechnungen.append("\n   Durchmesser: ").append(durchmesser);
				berechnungen.append("\n   Zentrum: [");

				for (int i = 0; i < zentrum.size(); i++)
					{
						if (i + 1 != zentrum.size())
							berechnungen.append(zentrum.get(i) + 1).append(",");
						else
							berechnungen.append(zentrum.get(i) + 1);
					}
				berechnungen.append("]").append("\n");
			}
		else
			berechnungen.append("   Der Graph ist nicht zusammenhängend.\n");

		berechnungen.append("   Komponentenanzahl: ").append(komponenten.size()).append("\n");
		berechnungen.append("   Komponenten: ");

		int i = 0;
		int k = 0;
		for (ArrayList<Integer> komp : komponenten)
			{
				berechnungen.append(i + 1).append(": [");

				for (Integer knoten : komponenten.get(i))
					{
						berechnungen.append(knoten + 1);
						if ((komponenten.get(i).indexOf(knoten) + 1) != komponenten.get(i).size())
							berechnungen.append(", ");

					}
				berechnungen.append("] ");

				if (komponenten.get(i).size() > 2)
					{
						String kante = "" + komponentenKanten.get(k);
						kante = kante.replaceFirst(Pattern.quote("["), "{ ");
						kante = kante.substring(0, kante.length() - 1) + " }";
						berechnungen.append(kante).append(" ");
						k++;
					}

				i++;
			}

		if (artikulationen.size() == 0)
			berechnungen.append("\n   Artikulationen: nicht vorhanden\n");
		else
			{
				berechnungen.append("\n   Artikulationen: [");
				for (Integer artikulation : artikulationen)
					{
						berechnungen.append(artikulation + 1);
						if ((artikulationen.indexOf(artikulation) + 1) != artikulationen.size())
							berechnungen.append(", ");

					}
				berechnungen.append("]\n");
			}

		berechnungen.append("   Brücken: ");

		boolean vorhanden = false;
		for (int z = 0; z < bruecken.length; z++)
			for (int s = 0; s < bruecken.length; s++)
				if (bruecken[z][s])
					{
						if (vorhanden)
							berechnungen.append(", ");
						vorhanden = true;
						berechnungen.append("[").append(z + 1).append(", ").append(s + 1).append("]");
					}
		if (!vorhanden)
			berechnungen.append("nicht vorhanden");

		long estimatedTime = System.currentTimeMillis() - startTime;
		berechnungen.append("\n\n   Ausführungszeit: " + estimatedTime + " ms");
		return berechnungen.toString();

	}

	private String matrixDrueck(String name, int[][] distanzmatrix)
	{
		if (matrix != null && name != null)
			{
				StringBuilder sb = new StringBuilder(1000);
				sb.append(name + "\n");

				for (int z = 0; z < matrix.length; z++)
					{
						for (int s = 0; s < matrix.length; s++)
							{
								if (distanzmatrix[z][s] == 0 && z != s)
									sb.append("   " + "u");
								else
									sb.append("   " + distanzmatrix[z][s]);
							}
						sb.append("\n");
					}
				sb.append(" ");
				for (int i = 0; i < matrix.length; i++)
					{
						sb.append("----");
					}
				sb.append("\n");
				return sb.toString();
			}
		return "";
	}
}
