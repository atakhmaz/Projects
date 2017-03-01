#include "shortestdistance.h"

using std::cin;

int main()
{
    char ans;
    do{
        ShortestDistance s("FinalData.xml", "airports.xml", "routes.xml", "airlines.xml");
        string airport1, airport2;
        cout << "What is your starting airport: ";
        cin >> airport1;
        cout << "What is your destination airport: ";
        cin >> airport2;

        vector<Vertex*> vec = s.shortestDistance(airport1,airport2);

        vector<Vertex*>::const_iterator it = vec.begin();
        if(!vec.empty())
        {
            for(; it+1 != vec.end(); ++it)
            {
                vector<Routes*>::const_iterator mt = (*it)->routes.begin();
                for(; mt != (*it)->routes.end(); ++ mt)
                {
                    if(((*(it+1))->airport_id) == (*mt)->nextAirport)
                    {
                        cout << endl << "With one of the following carriers: " << endl;
                        vector<string>::const_iterator mt2 = (*mt)->carriers.begin();
                        for(; mt2 != (*mt)->carriers.end(); ++ mt2)
                        {
                            cout << *mt2 << endl;
                        }
                    }
                }
                cout << endl;
                cout << "Go from " << (*it)->airport_iata << ": " << (*it)->airport_name << " Airport (" << (*it)->airport_city << ", " << (*it)->airport_country  << ")" << endl;
                cout << "Arrive at " << (*(it+1))->airport_iata << ": "  << (*(it+1))->airport_name << " Airport (" << (*(it+1))->airport_city << ", " << (*(it+1))->airport_country  << "), a total of " << (*(it+1))->cost << " miles." << endl;
            }
        }
        else
            cout << "No routes." << endl;
        cout << endl;
        cout << "Again?" << endl;
        cin >> ans;
    }while(toupper(ans) == 'Y');

    return 0;
}
