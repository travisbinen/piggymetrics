FROM mongo:7.0

ADD init.sh /init.sh
ADD ./dump /

RUN chmod +x /init.sh

ENTRYPOINT ["/init.sh"]
