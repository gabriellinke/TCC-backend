import easyocr
import logging
import argparse
import re

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required = True,
    help = "path to the image file")
args = vars(ap.parse_args())

# Configura o nível de logging para ERROR para suprimir avisos
logging.getLogger('easyocr').setLevel(logging.ERROR)
reader = easyocr.Reader(['pt'])
result = reader.readtext(args["image"], detail='False')

# Expressão regular para exatamente 6 dígitos numéricos
six_digit_pattern = re.compile(r'^\d{6}$')

for item in result:
    text = item[1]  # Penúltima string
    confidence = round(item[2], 2)  # Último número truncado para 2 casas decimais

    # Verifica se o texto contém exatamente 6 dígitos numéricos
    if six_digit_pattern.match(text):
        print(f"{text}, {confidence}")
        break