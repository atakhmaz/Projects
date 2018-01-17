#ifndef SHORTESTDISTANCE_H
#define SHORTESTDISTANCE_H

#include "containers.h"

class ShortestDistance
{
    public:
        ShortestDistance(char const* OutputFileName, char const* airportsFileName, char const* routesFileName, char const* airlinesFilename);
        vector<Vertex *> shortestDistance(string& airport1, string& airport2);

    private:
        map<int,Vertex*> dataMap;
        map<int,string> carrierNames;
        map<int,Vertex*> table;

        ifstream airports;
        ifstream routes;
        ifstream airlines;
        ofstream out;
        string line;
        char const* filename;

        string extract(const string& line);
        void parseAirports(map<int,Vertex*> &dataMap);
        void parseRoutes(map<int,Vertex*> &dataMap);
        void parseAirline(map<int,string> &carrierNames);
        void writeXML(map<int,Vertex*> &dataMap, map<int,string> &carrierNames);
        void createTable();
        void toUpper(string &aString);
};

#endif // SHORTESTDISTANCE_H
