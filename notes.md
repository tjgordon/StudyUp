Need to source bashrc after login to use docker compose for some reason:  
`source ~/.bashrc`  


Open new terminal in nginx container:   
(exec, not run, so that the same instance is used)  
`docker exec -it docker_database_1 bash`  

Hotswap:  
`docker exec -it docker_database_1 /root/hotswap.sh redis`  

