import socket
import struct
import sys


class BoggleClient:

    def __init__(self, host, port=42266):
        self.host = host
        self.port = port

    def run(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((self.host, self.port))

            DataIO.write_utf(s, 'Hello, world\n')

            other_player_name = DataIO.read_utf(s)
            print("Other Player Name: {}".format(other_player_name))

            board = self.decode_board(DataIO.read_utf(s))
            print("Board:\n{}".format('\n'.join(board)))

            words = set()
            while True:
                word = input("Enter word: ")
                if not word:
                    break
                words.add(word)

            DataIO.write_utf(s, '|'.join(words) + '\n')
            print("Your Words: {}".format(words))

            other_player_words = set(DataIO.read_utf(s).split('|'))
            print("Other Player Words: {}".format(other_player_words))

    def decode_board(self, encoded_board):
        w, h, letters = encoded_board.split(',')
        w = int(w)
        h = int(h)
        return [letters[i:i + w] for i in range(0, len(letters), w)]

class DataIO:

    @staticmethod
    def read_utf(s):
        bytes = bytearray()
        while True:
            b = s.recv(1)
            if not b:
                print("no more data from socket yet...")
                sys.exit(1)
            if b == b'\n':
                break
            bytes.extend(b)

        return bytes.decode()

    @staticmethod
    def write_utf(s, string):
        s.sendall(string.encode())

if __name__ == '__main__':
    BoggleClient(sys.argv[1]).run()
