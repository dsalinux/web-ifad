package br.edu.ifnmg.ifad.util;


public class StringHelper {

	/**
	 * Metodo remove caracters especiais (acentos, pontuações e etc.) de uma string;
	 * @since 12/12/2010
	 * </br> 1.0
	 * @author Danilo Souza Almeida
	 * @param texto
	 * @return
	 */
	public static String replaceAllSpecialCharacters(String texto){
		texto = texto.replaceAll("[ÂÀÁÄÃ]","A");
		texto = texto.replaceAll("[âãàáä]","a");  
		texto = texto.replaceAll("[ÊÈÉË]","E");  
		texto = texto.replaceAll("[êèéë]","e");  
		texto = texto.replaceAll("[ÎÍÌÏ]","I");  
		texto = texto.replaceAll("[îíìï]","i");  
		texto = texto.replaceAll("[ÔÕÒÓÖ]","O");  
		texto = texto.replaceAll("[ôõòóö]","o");  
		texto = texto.replaceAll("[ÛÙÚÜ]","U");  
		texto = texto.replaceAll("[ûúùü]","u");  
		texto = texto.replaceAll("[Ç]","C");  
		texto = texto.replaceAll("[ç]","c");   
		texto = texto.replaceAll("[ýÿ]","y");  
		texto = texto.replaceAll("[Ý]","Y");  
		texto = texto.replaceAll("[ñ]","n");  
		texto = texto.replaceAll("[Ñ]","N");  
		texto = texto.replaceAll("[^0-9a-zA-Z\\s]","");  
		return texto;  
	}
	
	/**
	 * Adiciona o caractere informado para a direita do texto até completar o tamanho informado.
	 * @param text
	 * @param character
	 * @param size
	 * @since 12/12/2010
	 * </br> 1.0
	 * @author Danilo Souza Almeida
	 * @return
	 */
	public static String addsCharacterRight(String text, char character, int size){
		if (text != null && text.length() < size){
			for (int i=text.length(); i < size; i++){
				text += character;
			}
		}
		return text;
	}

	/**
	 * Adiciona o caractere informado para a esquera do texto até completar o tamanho informado.
	 * @param text
	 * @param character
	 * @param size
	 * @since 12/12/2010
	 * </br> 1.0
	 * @author Danilo Souza Almeida
	 * @return
	 */
	public static String addsCharacterLeft(String text, char character, int size){
		String newText = "";
		if (text != null && text.length() < size){
			for (int i=text.length(); i < size; i++){
				newText += character;
			}
		}
		return newText+text;
	}
	
	/**
	 * Verifica se uma string é nula e/ou vazia. Usa Trim por default.
	 * @since 12/12/2010
	 * </br> 1.0
	 * @author Danilo Souza Almeida
	 * @return
	 */
	public static boolean isEmpty(String text){
		return StringHelper.isEmpty(text, true);
	}
	
	/**
	 * Verifica se uma string é nula e/ou vazia. O useTrim remove os espaçõs do inico e do final.
	 * @since 12/12/2010
	 * </br> 1.0
	 * @author Danilo Souza Almeida
	 * @return
	 */
	public static boolean isEmpty(String text, boolean useTrim){
		if (text == null){
			return true;
		}
		if (useTrim){
			text = text.trim();
		}
		if (text.equals("")){
			return true;
		}
		return false;
	}
        

    public static String getRandomPassword(final int length, boolean onlyLower) {
        if(length < 1){
            return "";
        }
        double upper = 123;
        double lower = 48;
        StringBuilder builder = new StringBuilder();
        for (int c = 0; c < length; c++) {
            int nRand;
            do {
                nRand = new Double((Math.random() * (upper - lower)) + lower).intValue();
            } while(!Character.isLetterOrDigit(nRand));
            builder.append((char)nRand);
        }
        String retorno = builder.toString();
        if(onlyLower){
            retorno = retorno.toLowerCase();
        }
        return retorno;
    }

    public static String getRandomPassword(final int length) {
            return getRandomPassword(length, true);
    }
}
