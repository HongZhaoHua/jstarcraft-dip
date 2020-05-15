/*
 * Do some test to ensure the java and c++ version behave similary when
 * stepping back and forth
 */

#include <iostream>
using namespace std;

//shall we typedef?

//Func headers. No reason to use a header file
void performAdvance(uint64_t delta, uint32_t iterations);
uint64_t pcg_advance_lcg_64(uint64_t state, uint64_t delta, uint64_t cur_mult,
		uint64_t cur_plus);


//State
static uint64_t  cur_step = 0;
static uint64_t state = 5;

int main() {

	cout << "C++" << endl << endl;

	//Single step forwards
	performAdvance(1,10);

	performAdvance(-1,10);

	return 0;
}

void performAdvance(uint64_t delta, uint32_t iterations) {
	uint64_t cur_mult = 6364136223846793005;
	uint64_t cur_plus = 16;

	for (uint32_t i = 0; i < iterations; i++) {
		state = pcg_advance_lcg_64(state, delta, cur_mult, cur_plus);
		cur_step += delta;
		cout << cur_step << " " << state << endl;
	}
}

//Original step ahead and back function used in c++
uint64_t pcg_advance_lcg_64(uint64_t state, uint64_t delta, uint64_t cur_mult,
		uint64_t cur_plus) {
	uint64_t acc_mult = 1u;
	uint64_t acc_plus = 0u;
	while (delta > 0) {
		if (delta & 1) {
			acc_mult *= cur_mult;
			acc_plus = acc_plus * cur_mult + cur_plus;
		}
		cur_plus = (cur_mult + 1) * cur_plus;
		cur_mult *= cur_mult;
		delta /= 2;
	}
	return acc_mult * state + acc_plus;
}
