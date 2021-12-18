import requests
from bs4 import BeautifulSoup
from nltk import word_tokenize
import sys
import os
from random import shuffle


def only_letters(word):
    for c in word:
        if not 'a' <= c <= 'z':
            return False
    return True


def get_parsed_text(text):
    return [word for word in word_tokenize(text.lower()) if len(word) > 3 and only_letters(word)]


def extract_text(link):
    req = requests.get(link)
    soup = BeautifulSoup(req.text, 'lxml')
    res = []
    for tag in soup.find_all('p'):
        if len(tag.text) > 1:
            res += get_parsed_text(tag.text)
    return res


def main():
    assert len(sys.argv) == 5, 'python data_generator.py output_path filter_path url_count filter_count'
    _, path, filter_path, n_urls, n_filter = sys.argv
    n_urls, n_filter = int(n_urls), int(n_filter)

    urls = open('wikiArticleList.txt').readlines()
    shuffle(urls)
    
    os.makedirs(path, exist_ok=True)
    words = []
    for i, url in enumerate(urls[:n_urls]):
        with open(path + '/input_%d' % i, 'w') as f:
            url = url.strip()
            f.write(url.split('/')[-1] + '\n')
            cur_words = extract_text(url)
            words += cur_words
            for word in cur_words:
                f.write(word + '\n')

    shuffle(words)
    with open(filter_path, 'w') as f:
        for word in words[:n_filter]:
            f.write(word + '\n')
    



if __name__ == '__main__':
    main()
