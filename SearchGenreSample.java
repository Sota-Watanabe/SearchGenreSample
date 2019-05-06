import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class SearchGenreSample {
	public static void main(String[] args) {
		Connection con = null;
		try {
		    // DBに接続する
			con = DriverManager.getConnection(
				"jdbc:mysql://localhost/gamedb?serverTimezone=JST", "root", "password");
			// System.out.println("MySQLに接続できました。");
			System.out.println("検索したい商品のジャンルを入力してください:");
			// Statementを作る
			Statement stm = con.createStatement();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("1:");
			String inputA = reader.readLine();
			System.out.println("2:");
			String inputB = reader.readLine();
			// 実行するSQLを文字列(String)にする
			String sql = "SELECT soft.softID,soft.name " +
					"FROM soft,genre,separate " +
					"WHERE soft.softID IN " +
					"(SELECT soft.softID FROM soft, separate, genre " +
					"WHERE separate.softID = soft.softID " +
					"AND separate.genreID = genre.genreID " +
					"AND genre.type = '"+inputA+"') " +
					"AND separate.softID = soft.softID " +
					"AND separate.genreID = genre.genreID " +
					"AND genre.type = '"+inputB+"';";


			// SQLを実行して、実行結果をResultSetに入れる
			ResultSet rs = stm.executeQuery(sql);

			// 結果を１行ずつ見て、必要な処理(ここでは表示)をする
			while(rs.next()){
				String name = rs.getString("name");
				System.out.println("取得結果 -> "  + name);
			}

		} catch (SQLException se) {
			System.out.println("SQL Error 1 : " + se.toString() + " "+ se.getErrorCode() + " " + se.getSQLState());
		}catch(IOException e){
			System.out.println("IO Error 2 :" + e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("MySQLのクローズに失敗しました。");
				}
			}
		}
	}
}
